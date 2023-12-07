// EventService.java
package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import com.sth.eventservice.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    // 전체 리스트
    public List<EventDTO> listEvent() {
        List<Event> list = eventRepository.findAll();
        List<EventDTO> resultList = new ArrayList<>();
        list.forEach(event -> resultList.add(event.toDto()));
        return resultList;
    }

    // 페이징 처리
    public Page<EventDTO> listEventPaging(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        return page.map(Event::toDto);
    }


    public void addEvent(EventDTO eventDTO) {
        eventRepository.save(eventDTO.toEntity());
    }

    // 이벤트 리스트 추가
    public void addEvents(List<EventDTO> eventDTOList) {
        eventDTOList.forEach(cityDTO -> eventRepository.save(cityDTO.toEntity()));
    }


    public EventDTO getEventsByEventNm(String eventNm) {
        Event event = eventRepository.findByEventNm(eventNm);

        if (event != null) {
            return event.toDto();
        }

        return null;

    }

    @Transactional
    public void saveEventsFromXml() {
        List<EventDTO> eventDTOList = callApiAndParseXml();
        for (EventDTO eventDTO : eventDTOList) {
            if (eventDTO != null) {
                eventRepository.save(eventDTO.toEntity());
            }
        }
    }

    public void saveEvents() {
        RestTemplate restTemplate = new RestTemplate();

        String areaApiUrl = "http://localhost:8000/api/v1/area-service/areas";
        AreaResponse[] areas = restTemplate.getForObject(areaApiUrl, AreaResponse[].class);

        for (AreaResponse area : areas) {
            int startPage = 1;
            int pageSize = 100;
            String areaname = area.getAreaNm();

            // areaNameFromSaveEvents에 값을 설정

            String apiUrl = "http://openapi.seoul.go.kr:8088/48435455656b617238305977625a75/xml/citydata/" + startPage + "/" + pageSize + "/" + areaname;

            try {
                ResponseEntity<EventResponse> responseEntity = restTemplate.getForEntity(apiUrl, EventResponse.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    EventResponse response = responseEntity.getBody();
                    if (response != null && response.getCitydata().getEvents() != null && !response.getCitydata().getEvents().isEmpty()) {
                        List<EventStts> eventSttsList = response.getCitydata().getEvents();
                        List<EventDTO> eventDTOList = eventSttsList.stream()
                                .map(eventStts -> EventDTO.builder()
                                        .areaNm(areaname)
                                        .eventNm(eventStts.getEVENT_NM())
                                        .build())
                                .collect(Collectors.toList());

                        addEvents(eventDTOList);

                        System.out.println("API 호출 성공 - 페이지: " + startPage);
                    } else {
                        System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                    }
                } else {
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private List<EventDTO> callApiAndParseXml() {
        int firstPage = 1;
        int lastPage = 1000;
        int maxPage = 4000;

        List<EventDTO> eventDTOList = new ArrayList<>(); // 여러 페이지의 데이터를 모아둘 리스트

        RestTemplate restTemplate = new RestTemplate();
        while (firstPage <= maxPage) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/71684451416f75723738574b486156/xml/culturalEventInfo/" + firstPage + "/" + (firstPage + lastPage - 1);
            try {
                ResponseEntity<EventResponseTotal> responseEntity = restTemplate.getForEntity(apiUrl, EventResponseTotal.class);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    EventResponseTotal response = responseEntity.getBody();

                    if (response != null && response.getEvents() != null && !response.getEvents().isEmpty()) {
                        List<EventDTO> eventsFromPage = response.getEvents().stream()
                                .map(eventdata -> {
                                    Event event = eventRepository.findByEventNm(eventdata.getTitle());
                                    if (event == null) {
                                        return null;
                                    }
                                    EventDTO eventDTO = event.toDto();
                                    eventDTO.setTitle(eventdata.getTitle());
                                    eventDTO.setCodename(eventdata.getCodeName());

                                    LocalDate startDate = LocalDate.parse(eventdata.getStartDate().substring(0, 10));
                                    LocalDate endDate = LocalDate.parse(eventdata.getEndDate().substring(0, 10));

                                    eventDTO.setStrtdate(startDate);
                                    eventDTO.setEndDate(endDate);
                                    eventDTO.setPlace(eventdata.getPlace());
                                    eventDTO.setUseFee(eventdata.getUseFee());
                                    eventDTO.setPlayer(eventdata.getPlayer());
                                    eventDTO.setProgram(eventdata.getProgram());
                                    eventDTO.setOrgLink(eventdata.getOrgLink());
                                    eventDTO.setLot(eventdata.getLat());
                                    eventDTO.setLat(eventdata.getLot());
                                    eventDTO.setGuname(eventdata.getGuname());
                                    eventDTO.setMainImg(eventdata.getMainImg());

                                    return eventDTO;
                                })
                                .collect(Collectors.toList());

                        eventDTOList.addAll(eventsFromPage);
                    }
                } else {
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            firstPage += lastPage;
        }
        return eventDTOList;
    }

}