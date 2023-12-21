package com.sth.sbikeservice.service;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.SbikeRepository;
import com.sth.sbikeservice.vo.RentBikeStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SbikeService {

    @Autowired
    private SbikeRepository sbikeRepository;

    public List<SbikeDTO> listSbike() {
        List<Sbike> list = sbikeRepository.findAll();
        List<SbikeDTO> resultList = new ArrayList<>();
        list.forEach(sbike -> resultList.add(sbike.toDto()));
        return resultList;
    }

    public Optional<Sbike> findSbikeByStationId(String stationId) {

        Optional<Sbike> sbikeDTO = sbikeRepository.findByStationId(stationId);
        return sbikeDTO;
    }

    @CircuitBreaker(name = "basicCircuitBreaker", fallbackMethod = "fallbackGetSbike")
    public void createSbike() {
        int firstData = 1;
        int lastData = 1000; // 한 페이지당 가져올 이벤트 수
        int maxPage = 3000; // 최대 페이지 수

        while (firstData <= maxPage) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/4d5967564d6f757233354443497375/json/bikeList/" + firstData + "/" + (firstData + lastData - 1);
            RestTemplate restTemplate = new RestTemplate();

            try {
                ResponseEntity<RentBikeStatus> responseEntity = restTemplate.getForEntity(apiUrl, com.sth.sbikeservice.vo.RentBikeStatus.class);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    com.sth.sbikeservice.vo.RentBikeStatus response = responseEntity.getBody();

                    if (response != null && response.getRentBikeStatus() != null && response.getRentBikeStatus().getRow() != null && !response.getRentBikeStatus().getRow().isEmpty()) {
                        List<SbikeDTO> sbikeDTOList = response.getRentBikeStatus().getRow();
                        addSbikeToDatabase(sbikeDTOList);
                        System.out.println("API 호출 성공 - 페이지: " + firstData);
                    } else {
                        System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                        System.out.println("API 응답 전체: " + responseEntity.getBody());
                        break;
                    }
                } else {
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                    System.out.println("API 응답 전체: " + responseEntity.getBody());
                    break;
                }
            } catch (Exception e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
                break;
            }
            firstData += lastData; // 이 부분이 반복문 밖으로 이동
        }
    }

    public void fallbackGetSbike(Exception e) {
        log.error("GetSbike 실행 중 예외 발생: " + e.getMessage());
    }

    @Transactional
    public void addSbikeToDatabase(List<SbikeDTO> sbikeDTOList) {
        for (SbikeDTO sbikeDTO : sbikeDTOList) {
            // stationName에서 숫자. 패턴을 제거
            String stationName = sbikeDTO.getStationName().replaceAll("^\\d+\\.\\s*", "");
            sbikeDTO.setStationName(stationName);

            Optional<Sbike> existingSbike = sbikeRepository.findByStationId(sbikeDTO.getStationId());

            if (existingSbike.isPresent()) {
                // 이미 있는 데이터면 업데이트
                Sbike sbikeToUpdate = existingSbike.get();
                updateSbikeFromDTO(sbikeToUpdate, sbikeDTO);
                sbikeRepository.save(sbikeToUpdate); // 업데이트 수행
            } else {
                // 새로운 데이터면 저장
                Sbike sbike = sbikeDTO.toEntity();
                sbikeRepository.save(sbike);
            }
        }
    }

    private void updateSbikeFromDTO(Sbike sbike, SbikeDTO sbikeDTO) {
        sbike.setRackTotCnt(sbikeDTO.getRackTotCnt());

        String stationName = sbikeDTO.getStationName().replaceAll("^\\d+\\.\\s*", "");

        sbike.setStationName(stationName);


        sbike.setParkingBikeTotCnt(sbikeDTO.getParkingBikeTotCnt());
        sbike.setShared(sbikeDTO.getShared());
        sbike.setStationLongitude(Double.parseDouble(sbikeDTO.getStationLongitude()));
        sbike.setStationLatitude(Double.parseDouble(sbikeDTO.getStationLatitude()));
    }
}