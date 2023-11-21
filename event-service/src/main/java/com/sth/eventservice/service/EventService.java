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
    public static class EventListResponse {
        private List<EventDTO> row;

        @XmlElement(name = "row")
        public void setRow(List<EventDTO> row) {
            this.row = row;
        }
    }

    public void updateEventsFromApi() {
        // API 호출 및 데이터 저장
        int startPage = 1;
        int pageSize = 100; // 한 페이지당 가져올 이벤트 수

        while (true) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/71684451416f75723738574b486156/xml/culturalEventInfo/" + startPage + "/" + pageSize + "/";
            RestTemplate restTemplate = new RestTemplate();

            try {
                // API 호출 및 응답을 ResponseEntity로 받음
                ResponseEntity<EventListResponse> responseEntity = restTemplate.getForEntity(apiUrl, EventListResponse.class);

                // API 호출이 성공한 경우
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    EventListResponse response = responseEntity.getBody();

                    // 여기서부터는 이벤트 정보를 처리하면 됩니다
                    if (response != null && response.getRow() != null && !response.getRow().isEmpty()) {
                        List<EventDTO> eventDTOList = response.getRow();
                        saveEventsToDatabase(eventDTOList);
                        System.out.println("API 호출 성공 - 페이지: " + startPage);
                    } else {
                        System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                        break;
                    }
                } else {
                    // API 호출이 실패한 경우
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                    break;
                }
            } catch (Exception e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace(); // 스택 트레이스 출력
                break;
            }

            // 다음 페이지로 이동
            startPage++;
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
