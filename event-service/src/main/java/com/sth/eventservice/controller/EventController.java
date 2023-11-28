package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;


import com.sth.eventservice.service.EventService;

import com.sth.eventservice.vo.AreaResponse;
import com.sth.eventservice.vo.Citydata;
import com.sth.eventservice.vo.EventResponse;
import com.sth.eventservice.vo.EventStts;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.StringHttpMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-service")
public class EventController {
    private final EventService eventService;

    @GetMapping("/")
    public String index() {
        return "hello, this is event service";
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getEvent() {
        Iterable<EventDTO> eventList = eventService.listEvent();

        List<EventDTO> result = new ArrayList<>();
        eventList.forEach(v -> {
            result.add(v);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/saveEvents")
    public void saveEvents() {
        RestTemplate restTemplate = new RestTemplate();

        String areaApiUrl = "http://localhost:8000/api/v1/area-service/areas";

        AreaResponse[] areas = restTemplate.getForObject(areaApiUrl, AreaResponse[].class);

        for(int i = 0; i < areas.length; i++) {
            // API 호출 및 데이터 저장
            int startPage = 1;
            int pageSize = 100; // 한 페이지당 가져올 이벤트 수

            String areaname = areas[i].getAreaNm();

            String apiUrl = "http://openapi.seoul.go.kr:8088/48435455656b617238305977625a75/xml/citydata/" + startPage + "/" + pageSize + "/" + areaname;
//            String apiUrl = "http://openapi.seoul.go.kr:8088/48435455656b617238305977625a75/xml/citydata/" + startPage + "/" + pageSize + "/광화문·덕수궁";

            try {
                // API 호출 및 응답을 ResponseEntity로 받음
                ResponseEntity<EventResponse> responseEntity = restTemplate.getForEntity(apiUrl, EventResponse.class);
//                return responseEntity;
                // API 호출이 성공한 경우
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    EventResponse response = responseEntity.getBody();
//                    return response.getCitydata().getEvents();

                    // 여기서부터는 이벤트 정보를 처리하면 됩니다
                    if (response != null && response.getCitydata().getEvents() != null && !response.getCitydata().getEvents().isEmpty()) {
                        List<EventStts> eventSttsList = response.getCitydata().getEvents();
                        List<EventDTO> eventDTOList = new ArrayList<>();

                        eventSttsList.forEach(eventStts -> {
                            eventDTOList.add(EventDTO.builder()
                                    .areaNm(areaname)
                                    .eventNm(eventStts.getEVENT_NM())
                                    .build());
                        });

                        eventService.addEvents(eventDTOList);

                        System.out.println("API 호출 성공 - 페이지: " + startPage);
                    } else {
                        System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                    }
                } else {
                    // API 호출이 실패한 경우
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace(); // 스택 트레이스 출력
            }
//        }
        }
    }
}

