package com.sth.sbikeservice.controller;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.schedule.KakaoApi;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.repository.KaKaoRepository;
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
    private final KakaoApi kakaoApi;
    private final KaKaoRepository kaKaoRepository;

    @Autowired
    public SbikeController(SbikeSchedule sbikeSchedule, SbikeService sbikeService, KakaoApi kakaoApi, KaKaoRepository kaKaoRepository) {
        this.sbikeSchedule = sbikeSchedule;
        this.sbikeService = sbikeService;
        this.kakaoApi = kakaoApi;
        this.kaKaoRepository = kaKaoRepository;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 공공API에서 따릉이 데이터 저장 /// ///////////////////////////////////////////////////////////////

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


    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 위도,경도에 따른 따릉이 정류장 저장 ///////////////////////////////////////////////////////////////

    @GetMapping("/getDistanceAndSaveToDB")
    public ResponseEntity<String> getDistanceAndSaveToDB() {
        kakaoApi.getDistanceAndSaveToDB();
        return ResponseEntity.ok("Get distance and save to DB successful");
    }





    @PostMapping("/saveDistance")
    public void saveDistance(@RequestParam String origin) {
        // 전체 정류장 정보를 읽어옴
        List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();

        // 각 정류장의 정보를 이용하여 Distance 메서드 호출
        for (SbikeDTO sbikeDTO : sbikeDTOList) {
            String destination = sbikeDTO.getStationLongitude() + "," + sbikeDTO.getStationLatitude();
            String stationName = sbikeDTO.getStationName();

            // 거리를 호출하고 저장하는 메서드 추가
            saveDistanceForStation(origin, destination, stationName);
        }
    }

    private void saveDistanceForStation(String origin, String destination, String stationName) {
        // 거리를 호출하고 반환
        int distance = kakaoApi.getDistance(origin, destination);

        // 거리를 이용하여 KaKao 엔티티 생성
        KaKao kaKao = KaKao.builder()
                .stationName(stationName)
                .origin(origin)
                .destination(destination)
                .distance(distance)
                .build();

        kaKaoRepository.save(kaKao);
    }


}