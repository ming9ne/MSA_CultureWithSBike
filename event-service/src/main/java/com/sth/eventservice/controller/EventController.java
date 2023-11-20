package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("event-service/api/v1")
public class EventController {
    private EventService eventService;

    @GetMapping("/updateFromApi")
    public void updateEventsFromApi() {
        eventService.updateEventsFromApi();
    }
    @GetMapping("/")
    public String index() {
        return "hello, this is congestion service";
    }
    @GetMapping("/getAll")
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }
}

