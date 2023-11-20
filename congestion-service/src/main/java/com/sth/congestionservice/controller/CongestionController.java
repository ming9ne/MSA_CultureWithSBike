package com.sth.congestionservice.controller;

import com.sth.congestionservice.model.dto.CongestionDTO;
import com.sth.congestionservice.model.dto.PopulationDTO;
import com.sth.congestionservice.service.CongestionService;
import com.sth.congestionservice.service.PopulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/congestion-service")
public class CongestionController {
    private final CongestionService congestionService;
    private final PopulationService populationService;

    @GetMapping("/")
    public String index() {
        return "hello, this is congestion service";
    }

    @GetMapping("/congestions")
    public ResponseEntity<List<CongestionDTO>> getCongestion() {
        Iterable<CongestionDTO> congestionList = congestionService.listCongestion();

        List<CongestionDTO> result = new ArrayList<>();
        congestionList.forEach(v -> {
            result.add(v);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/populations")
    public ResponseEntity<List<PopulationDTO>> getPopulation() {
        Iterable<PopulationDTO> populationList= populationService.listPopulation();

        List<PopulationDTO> result = new ArrayList<>();
        populationList.forEach(v -> {
            result.add(v);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
