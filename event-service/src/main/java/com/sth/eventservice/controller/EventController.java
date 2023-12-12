// EventController.java
package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.schedule.EventSchedule;
import com.sth.eventservice.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/event-service")
public class EventController {
    private final EventService eventService;
    private final EventSchedule eventSchedule;

    public EventController(EventService eventService, EventSchedule eventSchedule) {
        this.eventService = eventService;
        this.eventSchedule = eventSchedule;
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

    @GetMapping("/days")
    public ResponseEntity<Map<String, Object>> getEventCountByDayAndMonth() {
        return eventService.getEventCountByDayAndMonth();
    }



    @GetMapping("/area")
    public ResponseEntity<Map<String, Object>> getMonthlyEventByArea(@RequestParam int month) {
        return eventService.getMonthlyEventByArea(month);
    }





    @GetMapping("/events/{page}")
    public ResponseEntity<List<EventDTO>> getEventByPage(@PathVariable int page) {
        int size = 20;

        Pageable pageable = PageRequest.of(page - 1, size);

        Iterable<EventDTO> eventList = eventService.listEventPaging(pageable);
        List<EventDTO> result = new ArrayList<>();
        eventList.forEach(result::add);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/saveEvents")
    public void saveEvents(){eventSchedule.saveEvents();}

}