package com.sth.congestionservice.controller;

import com.sth.congestionservice.model.dto.CongestionDTO;
import com.sth.congestionservice.model.dto.PopulationDTO;
import com.sth.congestionservice.service.CongestionService;
import com.sth.congestionservice.service.PopulationService;
import com.sth.congestionservice.vo.AreaResponse;
import com.sth.congestionservice.vo.Citydata;
import com.sth.congestionservice.vo.CongestionResponse;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.StringHttpMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
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

    // 전체 혼잡도 조회
    @GetMapping("/congestions")
    public ResponseEntity<List<CongestionDTO>> listCongestions() {
        Iterable<CongestionDTO> congestionList = congestionService.listCongestion();

        List<CongestionDTO> result = new ArrayList<>();
        congestionList.forEach(v -> {
            result.add(v);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 전체 인구 조회
    @GetMapping("/populations")
    public ResponseEntity<List<PopulationDTO>> listPopulations() {
        Iterable<PopulationDTO> populationList = populationService.listPopulation();

        List<PopulationDTO> result = new ArrayList<>();
        populationList.forEach(v -> {
            result.add(v);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 지역이름으로 혼잡도 조회
    @GetMapping("/congestion/{areaNm}")
    public ResponseEntity<CongestionDTO> findCongestion(@PathVariable("areaNm") String areaNm) {
        CongestionDTO congestionDTO = congestionService.getCongestionByArea(areaNm);

        return ResponseEntity.status(HttpStatus.OK).body(congestionDTO);
    }

    // 지역이름으로 인구 조회
    @GetMapping("/population/{areaNm}")
    public ResponseEntity<PopulationDTO> findPopulation(@PathVariable("areaNm") String areaNm) {
        PopulationDTO populationDTO = populationService.getCongestionByArea(areaNm);

        return ResponseEntity.status(HttpStatus.OK).body(populationDTO);
    }
}
