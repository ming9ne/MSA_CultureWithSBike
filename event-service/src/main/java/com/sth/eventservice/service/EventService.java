package com.sth.eventservice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public EventService(EventRepository eventRepository, RestTemplate restTemplate) {
        this.eventRepository = eventRepository;
        this.restTemplate = restTemplate;

        // Add MappingJackson2HttpMessageConverter to support JSON mapping
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Transactional
    public void callAreaService() {
        String areaServiceUrl = "http://localhost:8000/api/v1/area-service/areas";

        ResponseEntity<List<Map<String, String>>> responseEntity = restTemplate.exchange(
                areaServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, String>>>() {}
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<Map<String, String>> areaList = responseEntity.getBody();
            saveEventsFromAreaList(areaList);
        } else {
            System.out.println("Area Service 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
        }
    }

    @Transactional
    public void saveEventsFromAreaList(List<Map<String, String>> areaList) {
        for (Map<String, String> areaData : areaList) {
            String area = areaData.get("areaNm");

            String apiUrl = "http://openapi.seoul.go.kr:8088/636f62694e6f757236364b49674759/xml/citydata/1/5/" + area;

            try {
                ResponseEntity<CITYDATA> responseEntity = restTemplate.getForEntity(apiUrl, CITYDATA.class);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    CITYDATA citydata = responseEntity.getBody();

                    if (citydata != null && citydata.getEventSttsList() != null && !citydata.getEventSttsList().isEmpty()) {
                        List<EVENT_STTS> eventSttsList = citydata.getEventSttsList();

                        for (EVENT_STTS eventStts : eventSttsList) {
                            EventDTO eventDTO = new EventDTO();
                            eventDTO.setAREA_NM(citydata.getAreaNm());
                            eventDTO.setEVENT_NM(eventStts.getEventNm());

                            saveEventsToDatabase(eventDTO);
                            System.out.println("API 호출 성공 - 지역: " + area + ", 이벤트: " + eventStts.getEventNm());
                        }
                    } else {
                        System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                    }
                }
            } catch (Exception e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void saveEventsToDatabase(EventDTO eventDTO) {
        Event event = eventDTO.toEntity();
        eventRepository.save(event);
    }

    static class CITYDATA {
        @JsonProperty("AREA_NM")
        private String areaNm;

        @JsonProperty("EVENT_STTS")
        private List<EVENT_STTS> eventSttsList;

        public String getAreaNm() {
            return areaNm;
        }

        public List<EVENT_STTS> getEventSttsList() {
            return eventSttsList;
        }
    }

    static class EVENT_STTS {
        @JsonProperty("EVENT_NM")
        private String eventNm;

        public String getEventNm() {
            return eventNm;
        }
    }
}
