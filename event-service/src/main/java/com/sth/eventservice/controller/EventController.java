// EventController.java
package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.service.EventService;
import com.sth.eventservice.vo.AreaResponse;
import com.sth.eventservice.vo.EventResponse;
import com.sth.eventservice.vo.EventStts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/event-service")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String index() {
        return "hello, this is event service";
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getEvent() {
        Iterable<EventDTO> eventList = eventService.listEvent();
        List<EventDTO> result = new ArrayList<>();
        eventList.forEach(result::add);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/saveEvents")
    public void saveEvents() {
        RestTemplate restTemplate = new RestTemplate();

        String areaApiUrl = "http://localhost:8000/api/v1/area-service/areas";
        AreaResponse[] areas = restTemplate.getForObject(areaApiUrl, AreaResponse[].class);

        for (AreaResponse area : areas) {
            int startPage = 1;
            int pageSize = 100;
            String areaname = area.getAreaNm();

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

                        eventService.addEvents(eventDTOList);

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

    @GetMapping("/saveEventsFromXml")
    public void saveEventsFromXml() {
        eventService.saveEventsFromXml();
    }
}
