package com.sth.eventservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Component
public class EventApiClient {
    @Value("${event.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public EventApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getEventXml() {
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
