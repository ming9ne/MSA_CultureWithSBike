package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import com.sth.eventservice.util.EventApiClient;
import com.sth.eventservice.util.EventXmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventApiClient eventApiClient;
    private final EventXmlParser eventXmlParser;

    @Autowired
    public EventService(EventRepository eventRepository, EventApiClient eventApiClient, EventXmlParser eventXmlParser) {
        this.eventRepository = eventRepository;
        this.eventApiClient = eventApiClient;
        this.eventXmlParser = eventXmlParser;
    }

    public void fetchAndSaveEvents() {
        String xmlData = eventApiClient.getEventXml();
        List<EventDTO> eventDTOList = eventXmlParser.parseXml(xmlData);

        for (EventDTO eventDTO : eventDTOList) {
            Event eventEntity = eventDTO.toEntity();
            eventRepository.save(eventEntity);
        }
    }
}
