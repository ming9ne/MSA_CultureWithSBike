package com.sth.sbikeservice.controller;

import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.schedule.KakaoApi;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.schedule.SbikeSchedule;
import com.sth.sbikeservice.service.KakaoService;
import com.sth.sbikeservice.service.SbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sbike-service")
public class SbikeController {

    private final SbikeSchedule sbikeSchedule;
    private final SbikeService sbikeService;
    private final KakaoApi kakaoApi;

    private final KakaoService kakaoService;

    @Autowired
    public SbikeController(SbikeSchedule sbikeSchedule, SbikeService sbikeService, KakaoApi kakaoApi,KakaoService kakaoService) {
        this.sbikeSchedule = sbikeSchedule;
        this.sbikeService = sbikeService;
        this.kakaoApi = kakaoApi;
        this.kakaoService = kakaoService;
    }



    @GetMapping("/createSbike")
    public ResponseEntity<String> createSbike() {
        sbikeService.createSbike();
        return ResponseEntity.ok("Update Sbike successful");
    }

    @PostMapping("/addSbikeToDatabase")
    public void addSbikeToDatabase(@RequestBody List<SbikeDTO> sbikeDTOList) {
        sbikeService.addSbikeToDatabase(sbikeDTOList);
    }

    @GetMapping("/sbikes")
    public ResponseEntity<List<SbikeDTO>> listSbike() {
        Iterable<SbikeDTO> sbikeList = sbikeService.listSbike();  // 수정된 부분
        List<SbikeDTO> result = new ArrayList<>();
        sbikeList.forEach(result::add);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/sbikes/{stationId}")
    public ResponseEntity<Optional<Sbike>> listSbikeByStationId(@PathVariable String stationId) {
        Optional<Sbike> sbikeOptional = sbikeService.findSbikeByStationId(stationId);
        return ResponseEntity.status(HttpStatus.OK).body(sbikeOptional);
    }



    // 가까운 따릉이 정류장 조회 API

    @GetMapping("/kakaos")
    public ResponseEntity<List<KakoDTO>> listKakao() {
        List<KakoDTO> result = kakaoService.listKakao();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/kakaos/{eventId}")
    public ResponseEntity<List<KakoDTO>> findKakaoByEventId(@PathVariable String eventId) {
        List<KakoDTO> resultList = kakaoService.listKakaoByEventId(eventId);
        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }


    // 따릉이 위도 경도 API
    @GetMapping("/createKakao")
    public ResponseEntity<String> createKakao() {
        kakaoService.createKakao();
        return ResponseEntity.ok("Create Kakao DB successful");
    }
}