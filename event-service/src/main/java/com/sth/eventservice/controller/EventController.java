package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("event-service/api/v1")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/updateFromApi")
    public ResponseEntity<String> updateEventsFromApi() {
        eventService.updateEventsFromApi();
        return ResponseEntity.ok("Update successful");
    }

    @GetMapping("/")
    public String index() {
        return "hello, this is congestion service";
    }

    @PostMapping("/saveEventsToDatabase")
    public void saveEventsToDatabase(@RequestBody List<EventDTO> eventDTOList) {
        eventService.saveEventsToDatabase(eventDTOList);
    }
}
