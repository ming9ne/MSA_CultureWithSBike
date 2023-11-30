package com.sth.sbikeservice.service;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.SbikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SbikeService {

    @Autowired
    private SbikeRepository sbikeRepository;

    public static class RentBikeStatus {
        private List<SbikeDTO> row;

        public List<SbikeDTO> getRow() {
            return row;
        }

        public void setRow(List<SbikeDTO> row) {
            this.row = row;
        }
    }

    public void updateEventsFromApi() {
        // API 호출 및 데이터 저장
        int startPage = 1;
        int pageSize = 1000; // 한 페이지당 가져올 이벤트 수

        while (true) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/4d5967564d6f757233354443497375/json/bikeList/" + startPage + "/" + pageSize + "/";
            RestTemplate restTemplate = new RestTemplate();

            try {
                // API 호출 및 응답을 ResponseEntity로 받음
                ResponseEntity<RentBikeStatus> responseEntity = restTemplate.getForEntity(apiUrl, RentBikeStatus.class);

                // API 호출이 성공한 경우
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    RentBikeStatus response = responseEntity.getBody();

                    // 여기서부터는 이벤트 정보를 처리
                    if (response != null && response.getRow() != null && !response.getRow().isEmpty()) {
                        List<SbikeDTO> sbikeDTOList = response.getRow();
                        saveSbikeToDatabase(sbikeDTOList);
                        System.out.println("API 호출 성공 - 페이지: " + startPage);
                    } else {
                        System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                        // 추가: 로그로 API 응답 전체 출력
                        System.out.println("API 응답 전체: " + responseEntity.getBody());
                        break;
                    }
                } else {
                    // API 호출이 실패한 경우
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                    // 추가: 로그로 API 응답 전체 출력
                    System.out.println("API 응답 전체: " + responseEntity.getBody());
                    break;
                }
            } catch (Exception e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                // 추가: 로그로 스택 트레이스 전체 출력
                e.printStackTrace(); // 스택 트레이스 출력
                break;
            }

            // 다음 페이지로 이동
            startPage++;
        }
    }

    @Transactional
    public void saveSbikeToDatabase(List<SbikeDTO> sbikeDTOList) {
        // DTO를 Entity로 변환하여 저장
        for (SbikeDTO sbikeDTO : sbikeDTOList) {
            Sbike sbike = sbikeDTO.toEntity();
            sbikeRepository.save(sbike);
        }
    }
}
