package com.sth.eventservice.controller;

import org.springframework.web.bind.annotation.RestController;
import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.EventEntity;
import com.sth.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {
    private  final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@RequestBody EventDTO eventDTO) {
        EventEntity savedEvent = eventService.saveEvent(eventDTO);
        return ResponseEntity.ok(savedEvent);
    }

}
