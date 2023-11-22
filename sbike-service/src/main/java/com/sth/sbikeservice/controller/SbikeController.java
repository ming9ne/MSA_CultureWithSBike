package com.sth.sbikeservice.controller;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.service.SbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sbike-service")
public class SbikeController {

    private final SbikeService sbikeService;

    @Autowired
    public SbikeController(SbikeService sbikeService) {
        this.sbikeService = sbikeService;
    }

    @GetMapping("/updateFromApi")
    public ResponseEntity<String> updateEventsFromApi() {
        sbikeService.updateEventsFromApi();
        return ResponseEntity.ok("Update successful");
    }

    @GetMapping("/")
    public String index() {
        return "hello, this is Sbike service";
    }

    @PostMapping("/saveSbikeToDatabase")
    public void saveSbikeToDatabase(@RequestBody List<SbikeDTO> sbikeDTOList) {
        sbikeService.saveSbikeToDatabase(sbikeDTOList);
    }
}
