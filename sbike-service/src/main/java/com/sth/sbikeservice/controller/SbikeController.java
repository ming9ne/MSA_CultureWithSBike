package com.sth.sbikeservice.controller;

import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.schedule.KakaoApi;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.repository.KaKaoRepository;
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
    private final KaKaoRepository kaKaoRepository;

    private final KakaoService kakaoService;

    @Autowired
    public SbikeController(SbikeSchedule sbikeSchedule, SbikeService sbikeService, KakaoApi kakaoApi, KaKaoRepository kaKaoRepository,KakaoService kakaoService) {
        this.sbikeSchedule = sbikeSchedule;
        this.sbikeService = sbikeService;
        this.kakaoApi = kakaoApi;
        this.kaKaoRepository = kaKaoRepository;
        this.kakaoService = kakaoService;
    }

    // 공공API에서 따릉이 데이터 저장 , 따릉이 리스트 조회

    @GetMapping("/createSbike")
    public ResponseEntity<String> createSbike() {
        sbikeSchedule.createSbike();
        return ResponseEntity.ok("Update Sbike successful");
    }

    @PostMapping("/addSbikeToDatabase")
    public void addSbikeToDatabase(@RequestBody List<SbikeDTO> sbikeDTOList) {
        sbikeSchedule.addSbikeToDatabase(sbikeDTOList);
    }

    @GetMapping("/listSbike")
    public ResponseEntity<List<SbikeDTO>> listSbike() {
        Iterable<SbikeDTO> sbikeList = sbikeService.listSbike();  // 수정된 부분
        List<SbikeDTO> result = new ArrayList<>();
        sbikeList.forEach(result::add);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/findSbike/{stationId}")
    public ResponseEntity<Optional<Sbike>> listSbikeByStationId(@PathVariable String stationId) {
        Optional<Sbike> sbikeOptional = sbikeService.findSbikeByStationId(stationId);
        return ResponseEntity.status(HttpStatus.OK).body(sbikeOptional);
    }



    // 가까운 따릉이 정류장 조회 API

    @GetMapping("/listKakao")
    public ResponseEntity<List<KakoDTO>> listKakao() {
        List<KakoDTO> result = kakaoService.listKakao();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/listKakao/{origin}")
    public ResponseEntity<List<KakoDTO>> listKakaoByOrigin(@PathVariable String origin) {
        List<KakoDTO> allKakaoData = kakaoService.listKakao();
        List<KakoDTO> filteredKakaoData = allKakaoData.stream()
                .filter(kakaoDTO -> kakaoDTO.getEventName().equals(origin))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(filteredKakaoData);
    }



    // 따릉이 위도 경도 API
    @GetMapping("/createKakao")
    public ResponseEntity<String> createKakao() {
        kakaoApi.createKakao();
        return ResponseEntity.ok("Create Kakao DB successful");
    }


    @PostMapping("/addDistance")
    public void addDistance(@RequestParam String eventName) {
        // 전체 정류장 정보를 읽어옴
        List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();

        // 각 정류장의 정보를 이용하여 Distance 메서드 호출
        for (SbikeDTO sbikeDTO : sbikeDTOList) {
            String destination = sbikeDTO.getStationLongitude() + "," + sbikeDTO.getStationLatitude();
            String stationName = sbikeDTO.getStationName();

            // 거리를 호출하고 저장하는 메서드 추가
            saveDistanceForStation(eventName, destination, stationName);
        }
    }

    private void saveDistanceForStation(String eventName, String destination, String stationName) {
        // 거리를 호출하고 반환
        int distance = kakaoApi.getDistance(eventName, destination);

        // 거리를 이용하여 KaKao 엔티티 생성
        KaKao kaKao = KaKao.builder()
                .stationName(stationName)
                .eventName(eventName)
                .build();
        kaKaoRepository.save(kaKao);
    }




}