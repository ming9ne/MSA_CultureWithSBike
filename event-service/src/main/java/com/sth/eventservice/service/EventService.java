// EventService.java
package com.sth.eventservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import com.sth.eventservice.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final EventRepository eventRepository;
//    public ResponseEntity<List<Object>> getEventCountByDayAndMonth;
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

    public Page<EventDTO> listEventPaging(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        return page.map(Event::toDto);
    }

    public void addEvent(EventDTO eventDTO) {
        eventRepository.save(eventDTO.toEntity());
    }

    public void addEvents(List<EventDTO> eventDTOList) {
        List<Event> eventList = eventDTOList.stream()
                .map(EventDTO::toEntity)
                .collect(Collectors.toList());
        eventRepository.saveAll(eventList);
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

    @CircuitBreaker(name = "basicCircuitBreaker", fallbackMethod = "fallbackForSaveEvents")
    public void saveEvents() {
        String areaApiUrl = "http://localhost:8000/api/v1/area-service/areas";
        AreaResponse[] areas = restTemplate.getForObject(areaApiUrl, AreaResponse[].class);

        List<EventDTO> eventDTOList = new ArrayList<>();
        logger.info("도시 데이터 호출 시작");
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

                        for (EventStts eventStts : eventSttsList) {
                            String eventName = eventStts.getEVENT_NM();

                            // 중복 검사: 이벤트 이름이 이미 데이터베이스에 존재하는지 확인
                            if (!eventRepository.existsByEventNm(eventName)) {
                                EventDTO eventDTO = EventDTO.builder()
                                        .areaNm(areaname)
                                        .eventNm(eventName)
                                        .build();
                                eventDTOList.add(eventDTO);
                            } else {
                                // 이미 존재하는 이벤트인 경우 콘솔과 로그에 메시지 출력
                                System.out.println("중복 이벤트 발견: " + eventName);
                                logger.info("중복 이벤트 발견: " + eventName);
                            }
                        }
                        System.out.println("API 호출 중");
                    } else {
                        logger.info("API 응답에서 이벤트 정보를 찾을 수 없습니다.");
                    }
                } else {
                    logger.warn("API 호출 실패");
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                logger.warn("API 호출 중 오류 발생");
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
        logger.info("도시 데이터 호출 끝");
        addEvents(eventDTOList);
    }
    public void fallbackForSaveEvents(Exception e) {
        logger.error("saveEvents 실행 중 예외 발생: " + e.getMessage());
    }

    @CircuitBreaker(name = "basicCircuitBreaker", fallbackMethod = "fallbackForCallApiAndParseXml")
    private List<EventDTO> callApiAndParseXml() {
        int firstPage = 1;
        int lastPage = 1000;
        int maxPage = 4000;
        List<EventDTO> eventDTOList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        logger.info("문화 행사 API 호출 시작");
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
                        logger.info("DB 저장 완료");
                    }
                } else {
                    logger.warn("API 호출 실패");
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                logger.warn("API 호출 실패");
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            firstPage += lastPage;
        }
        logger.info("문화행사 API 호출 끝");
        return eventDTOList;
    }
    public void fallbackForCallApiAndParseXml(Exception e) {
        logger.error("callApiAndParseXml 실행 중 예외 발생: " + e.getMessage());
    }

    //통계
    //////////////////////////////////////////////////////////////////////

    public ResponseEntity<Map<String, Object>> getEventCountByDayAndMonth() {
        Map<String, Object> result = new HashMap<>();

        // 현재 날짜 구하기
        LocalDate currentDate = LocalDate.now();

        // 현재 월 포함하여 7개의 월 계산
        List<String> monthList = new ArrayList<>();
        int currentMonthValue = currentDate.getMonthValue();
        for (int i = 0; i < 7; i++) {
            Month month = Month.of((currentMonthValue + i - 1) % 12 + 1); // 1부터 12까지 순환
            String monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault());
            monthList.add(monthName);
        }

        // 현재 주의 시작 날짜와 끝나는 날짜 계산
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = currentDate.with(DayOfWeek.SUNDAY);

        // 일별 이벤트 건수를 저장할 Map 초기화 (요일 순서대로)
        Map<String, Integer> eventCountByDayOfWeek = new LinkedHashMap<>();
        eventCountByDayOfWeek.put("월요일", 0);
        eventCountByDayOfWeek.put("화요일", 0);
        eventCountByDayOfWeek.put("수요일", 0);
        eventCountByDayOfWeek.put("목요일", 0);
        eventCountByDayOfWeek.put("금요일", 0);
        eventCountByDayOfWeek.put("토요일", 0);
        eventCountByDayOfWeek.put("일요일", 0);

        // 월별 이벤트 건수를 저장할 Map 초기화 (월 순서대로)
        Map<String, Integer> eventCountByMonth = new LinkedHashMap<>();
        for (String monthName : monthList) {
            eventCountByMonth.put(monthName, 0);
        }

        Iterable<EventDTO> eventList = listEvent();

        // 각 이벤트의 시작 날짜와 끝나는 날짜를 기준으로 요일 및 월을 계산하고 건수를 집계
        for (EventDTO event : eventList) {
            LocalDate startDate = event.getStrtdate();
            LocalDate endDate = event.getEndDate();
            if (startDate != null && endDate != null) {
                // 현재 주에 해당하는 이벤트만 집계
                if (!startDate.isAfter(endOfWeek) || !endDate.isBefore(startOfWeek)) {
                    // 요일 계산
                    DayOfWeek dayOfWeek = startDate.getDayOfWeek();
                    String dayOfWeekName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());

                    // 해당 요일의 이벤트 건수 증가
                    eventCountByDayOfWeek.put(dayOfWeekName, eventCountByDayOfWeek.get(dayOfWeekName) + 1);

                    // 월 계산
                    Month month = startDate.getMonth();
                    String monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault());

                    // 현재 월부터 7개의 월까지의 이벤트 건수 증가
                    if (monthList.contains(monthName)) {
                        eventCountByMonth.put(monthName, eventCountByMonth.getOrDefault(monthName, 0) + 1);
                    }
                }
            }
        }

        // 결과 맵에 현재 월부터 7개의 월까지의 월별 이벤트 건수와 현재 주의 일별 이벤트 건수를 추가
        result.put("Monthly Event", eventCountByMonth);
        result.put("Weekly Event", eventCountByDayOfWeek);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    ///////////////////////////////////////////////////////////////////////////////

    public ResponseEntity<Map<String, Object>> getMonthlyEventByArea() {
        // 현재 월을 가져오기
        Month currentMonth = LocalDate.now().getMonth();

        // eventService를 사용하여 모든 이벤트 데이터를 가져옵니다.
        List<EventDTO> eventList = listEvent();

        // 현재 월에 해당하는 이벤트 데이터 필터링
        List<EventDTO> currentMonthEvents = eventList.stream()
                .filter(event -> event.getStrtdate() != null && event.getEndDate() != null)
                .filter(event -> {
                    LocalDate startDateOfMonth = LocalDate.of(LocalDate.now().getYear(), currentMonth, 1);
                    LocalDate endDateOfMonth = startDateOfMonth.withDayOfMonth(startDateOfMonth.lengthOfMonth());
                    return !event.getEndDate().isBefore(startDateOfMonth) && !event.getStrtdate().isAfter(endDateOfMonth);
                })
                .collect(Collectors.toList());

        // 현재 월에 해당하는 지역별 이벤트 건수 계산
        Map<String, Integer> monthlyEventByArea = new HashMap<>();
        for (EventDTO event : currentMonthEvents) {
            String areaName = event.getAreaNm();
            int currentCount = monthlyEventByArea.getOrDefault(areaName, 0);
            monthlyEventByArea.put(areaName, currentCount + 1);
        }

        // 결과 맵에 지역구별 월별 이벤트 건수 추가
        Map<String, Object> result = new HashMap<>();
        result.put("areas", monthlyEventByArea);

        return ResponseEntity.ok(result);
    }
    ////////////////////////////당일 문화행사 조회

    public List<Event> listEventsWithCurrentDateStartDate(LocalDate currentDate) {
        return eventRepository.findByStrtdateLessThanEqualAndEndDateGreaterThanEqual(currentDate, currentDate);
    }
}