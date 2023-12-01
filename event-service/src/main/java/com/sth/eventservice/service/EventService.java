// EventService.java
package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import com.sth.eventservice.vo.EventResponse;
import com.sth.eventservice.vo.Eventdata;
import com.sth.eventservice.vo.EventResponseTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public EventService(EventRepository eventRepository, RestTemplate restTemplate) {
        this.eventRepository = eventRepository;
        this.restTemplate = restTemplate;
    }

    public List<EventDTO> listEvent() {
        List<Event> list = eventRepository.findAll();
        List<EventDTO> resultList = new ArrayList<>();
        list.forEach(event -> resultList.add(event.toDto()));
        return resultList;
    }

    public void addEvent(EventDTO eventDTO) {
        eventRepository.save(eventDTO.toEntity());
    }

    public void addEvents(List<EventDTO> eventDTOList) {
        eventDTOList.forEach(cityDTO -> eventRepository.save(cityDTO.toEntity()));
    }

    @Transactional
    public void saveEventsFromXml() {
        List<EventDTO> eventDTOList = callApiAndParseXml();
        for (EventDTO eventDTO : eventDTOList) {
            List<Event> existingEvent = eventRepository.findByEventNmAndTITLE(eventDTO.getEventNm(), eventDTO.getTITLE());
            if (existingEvent.isEmpty()) {
                eventRepository.save(eventDTO.toEntity());
            }
        }
    }

    private List<EventDTO> callApiAndParseXml() {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://openapi.seoul.go.kr:8088/71684451416f75723738574b486156/xml/culturalEventInfo/1/500/";

        try {
            ResponseEntity<EventResponseTotal> responseEntity = restTemplate.getForEntity(apiUrl, EventResponseTotal.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                EventResponseTotal response = responseEntity.getBody();

                if (response != null && response.getEvents() != null && !response.getEvents().isEmpty()) {
                    return response.getEvents().stream()
                            .map(eventdata -> EventDTO.builder()
//                                    .areaNm(eventdata.getGuname())
                                    .eventNm(eventdata.getTitle())
                                    .TITLE(eventdata.getTitle())
                                    .CODENAME(eventdata.getCodeName())
                                    .STRTDATE(eventdata.getStartDate())
                                    .END_DATE(eventdata.getEndDate())
                                    .PLACE(eventdata.getPlace())
                                    .USE_FEE(eventdata.getUseFee())
                                    .PLAYER(eventdata.getPlayer())
                                    .PROGRAM(eventdata.getProgram())
                                    .ORG_LINK(eventdata.getOrgLink())
                                    .LOT(eventdata.getLot())
                                    .LAT(eventdata.getLat())
                                    .GUNAME(eventdata.getGuname())
                                    .build())
                            .collect(Collectors.toList());
                } else {
                    return Collections.emptyList();
                }
            } else {
                System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.out.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}