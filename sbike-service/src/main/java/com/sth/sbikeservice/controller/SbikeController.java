package com.sth.sbikeservice.controller;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.schedule.SbikeSchedule;
import com.sth.sbikeservice.service.SbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sbike-service")
public class SbikeController {

    private final SbikeSchedule sbikeSchedule;
    private final SbikeService sbikeService;

    @Autowired
    public SbikeController(SbikeSchedule sbikeSchedule, SbikeService sbikeService) {
        this.sbikeSchedule = sbikeSchedule;
        this.sbikeService = sbikeService;
    }

    @GetMapping("/sbikeList")
    public ResponseEntity<List<SbikeDTO>> getSbike() {
        Iterable<SbikeDTO> sbikeList = sbikeService.listSbike();  // 수정된 부분
        List<SbikeDTO> result = new ArrayList<>();
        sbikeList.forEach(result::add);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/sbike")
    public ResponseEntity<String> get_sbike() {
        sbikeSchedule.get_sbike();
        return ResponseEntity.ok("Update Sbike successful");
    }

    @GetMapping("/")
    public String index() {
        return "hello, this is Sbike service";
    }

    @PostMapping("/saveSbikeToDatabase")
    public void saveSbikeToDatabase(@RequestBody List<SbikeDTO> sbikeDTOList) {
        sbikeSchedule.saveSbikeToDatabase(sbikeDTOList);
    }
}
