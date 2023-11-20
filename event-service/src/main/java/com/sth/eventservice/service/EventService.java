package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final String apiKey;

    public EventService(EventRepository eventRepository, @Value("${api.key}") String apiKey) {
        this.eventRepository = eventRepository;
        this.apiKey = apiKey;
    }

    @Transactional
    public void updateEventsFromApi() {
        String apiUrl = "http://openapi.seoul.go.kr:8088/" + apiKey + "/xml/culturalEventInfo/1/10/";
        RestTemplate restTemplate = new RestTemplate();
        String xmlData = restTemplate.getForObject(apiUrl, String.class);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EventDTO.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            List<EventDTO> events = Arrays.asList(((EventDTO[]) unmarshaller.unmarshal(new StringReader(xmlData))));

            List<Event> eventEntities = events.stream()
                    .map(EventDTO::toEntity)
                    .collect(Collectors.toList());

            eventRepository.saveAll(eventEntities);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(Event::toDto)
                .collect(Collectors.toList());
    }
}
