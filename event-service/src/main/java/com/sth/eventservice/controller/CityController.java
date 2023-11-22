package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.CityDTO;
import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.service.CityService;
import com.sth.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-service")
public class CityController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }
    @GetMapping("/addCityFromApi")
    public ResponseEntity<String> updateEventsFromApi() {
        cityService.addCityFromApi();
        return ResponseEntity.ok("City API add successful");
    }
    @PostMapping("/saveEventsToDatabase")
    public void saveCitiesToDatabase(@RequestBody List<CityDTO> cityDTOList) {
        cityService.saveCitiesToDatabase(cityDTOList);
    }
}
