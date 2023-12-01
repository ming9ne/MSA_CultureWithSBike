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
import java.util.Optional;

@Service
public class SbikeService {

    @Autowired
    private SbikeRepository sbikeRepository;
//    @Transactional
//    public void updateEventsFromApi() {
//        // 첫 번째 실행
//        fetchDataAndUpdateDatabase(1, 1000);
//
//        // 두 번째 실행
//        fetchDataAndUpdateDatabase(1001, 2000);
//
//        // 세 번째 실행
//        fetchDataAndUpdateDatabase(2001, 3000);
//    }
//
//    private void fetchDataAndUpdateDatabase(int startItem, int endItem) {
//        int lastData = endItem - startItem + 1; // 한 페이지당 가져올 이벤트 수
//        String apiUrl = "http://openapi.seoul.go.kr:8088/4d5967564d6f757233354443497375/json/bikeList/" + startItem + "/" + endItem;
//        RestTemplate restTemplate = new RestTemplate();
//
//        try {
//            ResponseEntity<RentBikeStatus> responseEntity = restTemplate.getForEntity(apiUrl, RentBikeStatus.class);
//
//            if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                RentBikeStatus response = responseEntity.getBody();
//
//                if (response != null && response.getRentBikeStatus() != null && response.getRentBikeStatus().getRow() != null && !response.getRentBikeStatus().getRow().isEmpty()) {
//                    List<SbikeDTO> sbikeDTOList = response.getRentBikeStatus().getRow();
//                    saveSbikeToDatabase(sbikeDTOList);
//                    System.out.println("API 호출 성공 - 범위: " + startItem + " ~ " + endItem);
//                } else {
//                    System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
//                    System.out.println("API 응답 전체: " + responseEntity.getBody());
//                }
//            } else {
//                System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
//                System.out.println("API 응답 전체: " + responseEntity.getBody());
//            }
//        } catch (Exception e) {
//            System.out.println("API 호출 중 오류 발생: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }


    public void get_sbike() {
        int firstData = 1;
        int lastData = 1000; // 한 페이지당 가져올 이벤트 수
        int maxPage = 3000; // 최대 페이지 수

        while (firstData <= maxPage) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/4d5967564d6f757233354443497375/json/bikeList/" + firstData + "/" + (firstData + lastData - 1);
            RestTemplate restTemplate = new RestTemplate();

            try {
                ResponseEntity<RentBikeStatus> responseEntity = restTemplate.getForEntity(apiUrl, RentBikeStatus.class);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    RentBikeStatus response = responseEntity.getBody();

                    if (response != null && response.getRentBikeStatus() != null && response.getRentBikeStatus().getRow() != null && !response.getRentBikeStatus().getRow().isEmpty()) {
                        List<SbikeDTO> sbikeDTOList = response.getRentBikeStatus().getRow();
                        saveSbikeToDatabase(sbikeDTOList);
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




    @Transactional
    public void saveSbikeToDatabase(List<SbikeDTO> sbikeDTOList) {
        // DTO를 Entity로 변환하여 저장
        for (SbikeDTO sbikeDTO : sbikeDTOList) {
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
        sbike.setStationName(sbikeDTO.getStationName());
        sbike.setParkingBikeTotCnt(sbikeDTO.getParkingBikeTotCnt());
        sbike.setShared(sbikeDTO.getShared());
        sbike.setStationLongitude(sbikeDTO.getStationLongitude());
        sbike.setStationLatitude(sbikeDTO.getStationLatitude());
    }

    public static class RentBikeStatus {
        private RentBikeInfo rentBikeStatus;

        public RentBikeInfo getRentBikeStatus() {
            return rentBikeStatus;
        }

        public void setRentBikeStatus(RentBikeInfo rentBikeStatus) {
            this.rentBikeStatus = rentBikeStatus;
        }

        public static class RentBikeInfo {
            private List<SbikeDTO> row;

            public List<SbikeDTO> getRow() {
                return row;
            }

            public void setRow(List<SbikeDTO> row) {
                this.row = row;
            }
        }
    }
}
