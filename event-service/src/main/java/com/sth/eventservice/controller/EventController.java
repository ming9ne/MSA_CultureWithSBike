// EventController.java
package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.schedule.EventSchedule;
import com.sth.eventservice.service.EventService;
import com.sth.eventservice.vo.AreaResponse;
import com.sth.eventservice.vo.EventResponse;
import com.sth.eventservice.vo.EventStts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final EventSchedule eventSchedule;

    public EventController(EventService eventService, EventSchedule eventSchedule) {
        this.eventService = eventService;
        this.eventSchedule = eventSchedule;
    }

    @GetMapping("/")
    public String index() {
        return "hello, this is event service";
    }

//    @GetMapping("/events")
//    public ResponseEntity<List<EventDTO>> getEvent() {
//        Iterable<EventDTO> eventList = eventService.listEvent();
//        List<EventDTO> result = new ArrayList<>();
//        eventList.forEach(result::add);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }

    @GetMapping("/events")
    public ResponseEntity<Page<EventDTO>> getEvent(Pageable pageable) {
        Page<EventDTO> eventPage = eventService.listEvent(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(eventPage);
    }


    @GetMapping("/saveEvents")
    public void saveEvents(){eventSchedule.saveEvents();}

    @GetMapping("/saveEventsFromXml")
    public void saveEventsFromXml() {
        eventSchedule.saveEventsFromXml();
    }
}