package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // 혼잡도 조회
    public List<EventDTO> listEvent() {
        List<Event> list = new ArrayList<>();
        List<EventDTO> resultList = new ArrayList<>();

        list = eventRepository.findAll();
        for(Event event : list) {
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
