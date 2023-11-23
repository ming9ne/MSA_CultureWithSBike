package com.sth.eventservice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import com.sth.eventservice.vo.AreaResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public EventService(EventRepository eventRepository, RestTemplate restTemplate) {
        this.eventRepository = eventRepository;
        this.restTemplate = restTemplate;

        // JSON 매핑을 지원하기 위해 MappingJackson2HttpMessageConverter 추가
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Transactional
    public void callAreaServiceAndSaveEvents() {
        String areaServiceUrl = "http://localhost:8000/api/v1/area-service/areas";

        // 외부 API에서 지역 목록을 가져옴
        AreaResponse[] areas = restTemplate.getForObject(areaServiceUrl, AreaResponse[].class);

        System.out.println("받아온 지역 목록:");
        for (AreaResponse area : areas) {
            System.out.println(area.getAreaNm());
        }

        // 지역 목록을 순회
        for (int i = 0; i < areas.length; i++) {
            int startPage = 1;
            int pageSize = 100;

            // 외부 API에 사용될 URL 생성
            String apiUrl = "http://openapi.seoul.go.kr:8088/636f62694e6f757236364b49674759/xml/citydata/"
                    + startPage + "/" + pageSize + "/" + areas[i].getAreaNm();

            try {
                // XML 요청을 위한 헤더 설정
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_XML);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                // 외부 API 호출
                ResponseEntity<CITYDATA> responseEntity = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<CITYDATA>() {}
                );

                // 외부 API 호출이 성공한 경우
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    CITYDATA citydata = responseEntity.getBody();

                    // 가져온 이벤트 데이터 처리
                    if (citydata != null && citydata.getEventSttsWrapper() != null && citydata.getEventSttsWrapper().getEventSttsList() != null) {
                        List<EVENT_STTS> eventSttsList = citydata.getEventSttsWrapper().getEventSttsList();

                        for (EVENT_STTS eventStts : eventSttsList) {
                            EventDTO eventDTO = new EventDTO();
                            eventDTO.setAREA_NM(citydata.getAreaNm());
                            eventDTO.setEVENT_NM(eventStts.getEventNm());

                            // 이벤트를 데이터베이스에 저장
                            saveEventsToDatabase(List.of(eventDTO));
                            System.out.println("API 호출 성공 - 지역: " + areas[i].getAreaNm() + ", 이벤트: " + eventStts.getEventNm());
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
    public void saveEventsToDatabase(List<EventDTO> eventDTOList) {
        for (EventDTO eventDTO : eventDTOList) {
            Event event = eventDTO.toEntity();
            eventRepository.save(event);
        }
    }

    static class CITYDATA {
        @JsonProperty("AREA_NM")
        private String areaNm;

        @JsonProperty("EVENT_STTS")
        private EVENT_STTSWrapper eventSttsWrapper;

        public String getAreaNm() {
            return areaNm;
        }

        public EVENT_STTSWrapper getEventSttsWrapper() {
            return eventSttsWrapper;
        }
    }

    static class EVENT_STTSWrapper {
        @JsonProperty("EVENT_STTS")
        private List<EVENT_STTS> eventSttsList;

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
