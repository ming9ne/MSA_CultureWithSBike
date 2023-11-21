// EventService.java

package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Data
    @XmlRootElement(name = "culturalEventInfo")
    private static class EventListResponse {
        private List<EventDTO> culturalEventInfo;

        @XmlElement(name = "row")
        public void setCulturalEventInfo(List<EventDTO> culturalEventInfo) {
            this.culturalEventInfo = culturalEventInfo;
        }
    }

    public void updateEventsFromApi() {
        // API 호출 및 데이터 저장
        String apiUrl = "http://openapi.seoul.go.kr:8088/71684451416f75723738574b486156/xml/culturalEventInfo/1/5/";
        RestTemplate restTemplate = new RestTemplate();

        try {
            // API 호출 및 응답을 ResponseEntity로 받음
            ResponseEntity<EventListResponse> responseEntity = restTemplate.getForEntity(apiUrl, EventListResponse.class);

            // API 호출이 실패한 경우에 대한 처리
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                EventListResponse response = responseEntity.getBody();

                if (response.getCulturalEventInfo() != null) {
                    List<EventDTO> eventDTOList = response.getCulturalEventInfo();
                    saveEventsToDatabase(eventDTOList);
                    System.out.println("API 호출 성공");
                } else {
                    System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                }
            } else {
                System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            System.out.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스 출력
        }
    }

    @Transactional
    public void saveEventsToDatabase(List<EventDTO> eventDTOList) {
        // DTO를 Entity로 변환하여 저장
        for (EventDTO eventDTO : eventDTOList) {
            Event event = eventDTO.toEntity();
            eventRepository.save(event);
        }
    }
}
