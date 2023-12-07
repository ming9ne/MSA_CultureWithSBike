// EventSchedule.java
package com.sth.eventservice.schedule;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import com.sth.eventservice.service.EventService;
import com.sth.eventservice.vo.AreaResponse;
import com.sth.eventservice.vo.EventResponse;
import com.sth.eventservice.vo.EventResponseTotal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSchedule {
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Scheduled(fixedDelay = 6 * 60 * 60 * 1000)
    public void saveEvents() {
        log.info("Scheduled Task: saveEvents");
        eventService.saveEvents();
        eventService.saveEventsFromXml();
    }

    // 초기 지연 5분 후 6시간마다
    @Scheduled(initialDelay = 5 * 60 * 1000, fixedDelay = 6 * 60 * 60 * 1000)
    public void saveEventsFromXml() {
        log.info("Scheduled Task: saveEventsFromXml");
        eventService.saveEventsFromXml();
    }



}
