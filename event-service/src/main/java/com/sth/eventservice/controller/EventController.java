// EventController.java
package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.schedule.EventSchedule;
import com.sth.eventservice.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/event-service")
public class EventController {
    private final EventService eventService;
    private final EventSchedule eventSchedule;

    public EventController(EventService eventService, EventSchedule eventSchedule) {
        this.eventService = eventService;
        this.eventSchedule = eventSchedule;
    }

    // 도시데이터에서 데이터 받아서 문화행사 정보 저장

    @GetMapping("/createEvents")
    public void createEvents() {
        eventService.saveEvents();
        eventService.saveEventsFromXml();
    }


    // 이벤트 리스트, 통계 조회
    /////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> listEvent() {
        Iterable<EventDTO> eventList = eventService.listEvent();
        List<EventDTO> result = new ArrayList<>();
        eventList.forEach(result::add);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 이벤트 리스트 페이지별 조회
    @GetMapping("/events/{page}")
    public ResponseEntity<List<EventDTO>> listEventByPage(@PathVariable int page) {
        int size = 20;

        Pageable pageable = PageRequest.of(page - 1, size);

        Iterable<EventDTO> eventList = eventService.listEventPaging(pageable);
        List<EventDTO> result = new ArrayList<>();
        eventList.forEach(result::add);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    // 주별, 월별 문화행사 건수 조회
    @GetMapping("/date")
    public ResponseEntity<Map<String, Object>> listWeeklyAndMonthly() {
        return eventService.getEventCountByDayAndMonth();
    }

    // 해당 월 문화행사 별 건수 조회
    @GetMapping("/event_area")
    public ResponseEntity<Map<String, Object>> listMonthlyEventByArea() {
        return eventService.getMonthlyEventByArea();
    }

    // ("/days") + ("/area") 합친 API
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> listStatistics() {
        ResponseEntity<Map<String, Object>> dayAndMonthResponse = eventService.getEventCountByDayAndMonth();
        ResponseEntity<Map<String, Object>> monthlyByAreaResponse = eventService.getMonthlyEventByArea();

        Map<String, Object> responseData = new HashMap<>();

        if (dayAndMonthResponse != null && monthlyByAreaResponse != null) {
            Map<String, Object> dayAndMonthData = dayAndMonthResponse.getBody();
            Map<String, Object> monthlyByAreaData = monthlyByAreaResponse.getBody();

            responseData.putAll(dayAndMonthData);
            responseData.putAll(monthlyByAreaData);
        }

        return ResponseEntity.ok(responseData);
    }

    // 당일 열리는 문화행사 리스트 조회
    @GetMapping("/todayEvents")
    public ResponseEntity<List<Event>> listTodayEvents() {
        LocalDate currentDate = LocalDate.now();
        List<Event> events = eventService.listEventsWithCurrentDateStartDate(currentDate);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }


}