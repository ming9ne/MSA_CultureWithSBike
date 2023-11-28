package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public EventService(EventRepository eventRepository, RestTemplate restTemplate) {
        this.eventRepository = eventRepository;
        this.restTemplate = restTemplate;
    }

    public void callApiAndSaveEvents() {
        String apiUrl = "http://openapi.seoul.go.kr:8088/71684451416f75723738574b486156/xml/culturalEventInfo/1/50";

        try {
            // XML 응답을 언마샬링하기 위한 JAXBContext 생성
            JAXBContext jaxbContext = JAXBContext.newInstance(CulturalEventInfo.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // 외부 API 호출
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // 외부 API 호출이 성공한 경우
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String xmlResponse = responseEntity.getBody();

                // XML을 Java 객체로 언마샬링
                CulturalEventInfo culturalEventInfo = (CulturalEventInfo) unmarshaller.unmarshal(new StringReader(xmlResponse));

                // 가져온 이벤트 데이터 처리
                if (culturalEventInfo != null && culturalEventInfo.getRows() != null) {
                    List<CulturalEventInfo.Row> rows = culturalEventInfo.getRows();

                    for (CulturalEventInfo.Row row : rows) {
                        EventDTO eventDTO = new EventDTO();
                        eventDTO.setAreaNm(row.getGUNAME());
                        eventDTO.setEventNm(row.getTITLE());
                        eventDTO.setCODENAME(row.getCODENAME());
                        eventDTO.setSTRTDATE(row.getSTRTDATE());
                        eventDTO.setEND_DATE(row.getEND_DATE());
                        eventDTO.setPLACE(row.getPLACE());
                        eventDTO.setPROGRAM(row.getPROGRAM());
                        eventDTO.setORG_LINK(row.getORG_LINK());
                        eventDTO.setLOT(row.getLOT());
                        eventDTO.setLAT(row.getLAT());

                        // 이벤트를 데이터베이스에 저장 (event_nm과 TITLE이 모두 일치해야 저장)
                        if (eventDTO.getEventNm() != null) {
                            List<Event> existingEvents = eventRepository.findByEventNmAndTITLE(
                                    eventDTO.getEventNm(),
                                    eventDTO.getTitle()
                            );

                            // 기존 데이터와 비교하여 중복 여부 확인
                            if (existingEvents.isEmpty()) {
                                saveEventsToDatabase(List.of(eventDTO));
                                System.out.println("이벤트 정보 저장 - 지역: " + eventDTO.getAreaNm() + ", 이벤트: " + eventDTO.getEventNm());
                            } else {
                                System.out.println("중복된 이벤트 정보를 찾았습니다");

                                // 기존 데이터와 일치하는 경우 해당 데이터의 나머지 필드 업데이트
                                for (Event existingEvent : existingEvents) {
                                    existingEvent.setCODENAME(eventDTO.getCODENAME());
                                    existingEvent.setSTRTDATE(eventDTO.getSTRTDATE());
                                    existingEvent.setEND_DATE(eventDTO.getEND_DATE());
                                    existingEvent.setPLACE(eventDTO.getPLACE());
                                    existingEvent.setPROGRAM(eventDTO.getPROGRAM());
                                    existingEvent.setORG_LINK(eventDTO.getORG_LINK());
                                    existingEvent.setLOT(eventDTO.getLOT());
                                    existingEvent.setLAT(eventDTO.getLAT());
                                    existingEvent.setGUNAME(eventDTO.getGUNAME());

                                    eventRepository.save(existingEvent);
                                    System.out.println("이벤트 정보 업데이트 완료 - 이벤트: " + existingEvent.getEventNm());
                                }
                            }
                        } else {
                            System.out.println("이벤트 정보를 찾을 수 없습니다");
                        }
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

    @Transactional
    public void saveEventsToDatabase(List<EventDTO> eventDTOList) {
        for (EventDTO eventDTO : eventDTOList) {
            Event event = eventDTO.toEntity();
            eventRepository.save(event);
        }
    }

    @XmlRootElement(name = "culturalEventInfo")
    static class CulturalEventInfo {
        private List<Row> row = new ArrayList<>();  // 빈 리스트로 초기화

        @XmlElement(name = "row")
        public void setRow(List<Row> row) {
            this.row = row;
        }

        public List<Row> getRows() {
            return row;
        }

        static class Row {
            private String GUNAME;
            private String TITLE;
            private String CODENAME;
            private String STRTDATE;
            private String END_DATE;
            private String PLACE;
            private String USE_FEE;
            private String PLAYER;
            private String PROGRAM;
            private String ORG_LINK;
            private Double LOT;
            private Double LAT;

            // Getter 및 Setter 메서드는 필요에 따라 추가

            public String getGUNAME() {
                return GUNAME;
            }

            public void setGUNAME(String GUNAME) {
                this.GUNAME = GUNAME;
            }

            public String getTITLE() {
                return TITLE;
            }

            public void setTITLE(String TITLE) {
                this.TITLE = TITLE;
            }

            public String getCODENAME() {
                return CODENAME;
            }

            public void setCODENAME(String CODENAME) {
                this.CODENAME = CODENAME;
            }

            public String getSTRTDATE() {
                return STRTDATE;
            }

            public void setSTRTDATE(String STRTDATE) {
                this.STRTDATE = STRTDATE;
            }

            public String getEND_DATE() {
                return END_DATE;
            }

            public void setEND_DATE(String END_DATE) {
                this.END_DATE = END_DATE;
            }

            public String getPLACE() {
                return PLACE;
            }

            public void setPLACE(String PLACE) {
                this.PLACE = PLACE;
            }

            public String getUSE_FEE() {
                return USE_FEE;
            }

            public void setUSE_FEE(String USE_FEE) {
                this.USE_FEE = USE_FEE;
            }

            public String getPLAYER() {
                return PLAYER;
            }

            public void setPLAYER(String PLAYER) {
                this.PLAYER = PLAYER;
            }

            public String getPROGRAM() {
                return PROGRAM;
            }

            public void setPROGRAM(String PROGRAM) {
                this.PROGRAM = PROGRAM;
            }

            public String getORG_LINK() {
                return ORG_LINK;
            }

            public void setORG_LINK(String ORG_LINK) {
                this.ORG_LINK = ORG_LINK;
            }

            public Double getLOT() {
                return LOT;
            }

            public void setLOT(Double LOT) {
                this.LOT = LOT;
            }

            public Double getLAT() {
                return LAT;
            }

            public void setLAT(Double LAT) {
                this.LAT = LAT;
            }
        }
    }

    // 혼잡도 조회
    public List<EventDTO> listEvent() {
        List<Event> list = eventRepository.findAll();
        List<EventDTO> resultList = new ArrayList<>();

        for (Event event : list) {
            resultList.add(event.toDto());
        }

        return resultList;
    }

    // 혼잡도 추가
    public void addEvent(EventDTO eventDTO) {
        eventRepository.save(eventDTO.toEntity());
    }

    // 혼잡도 여러개 추가
    public void addEvents(List<EventDTO> eventDTOList) {
        eventDTOList.forEach(cityDTO -> {
            eventRepository.save(cityDTO.toEntity());
        });
    }
}
