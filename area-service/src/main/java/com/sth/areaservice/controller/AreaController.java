package com.sth.areaservice.controller;

import com.sth.areaservice.model.dto.AreaDTO;
import com.sth.areaservice.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/area-service")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping("/")
    public String hello() {
        return "hello, area service!";
    }

    @GetMapping("/areas")
    public List<AreaDTO> listAreas() {
        return areaService.listArea();
    }
}
