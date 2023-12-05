package com.sth.sbikeservice.schedule;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.SbikeRepository;
import com.sth.sbikeservice.repository.KaKaoRepository;
import com.sth.sbikeservice.service.SbikeService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Component
public class SbikeSchedule {

    private final SbikeService sbikeService;
    private final SbikeRepository sbikeRepository;
    private static KaKaoRepository kaKaoRepository = null;
    @Autowired
    public SbikeSchedule(SbikeService sbikeService, SbikeRepository sbikeRepository, KaKaoRepository kaKaoRepository) {
        this.sbikeService = sbikeService;
        this.sbikeRepository = sbikeRepository;
        SbikeSchedule.kaKaoRepository = kaKaoRepository;
    }




    //    @Scheduled(fixedDelay = 300000)
    public void hello() {
        log.info("Sbike Scheduler");
    }
    //    @Scheduled(fixedDelay = 300000)
    public void get_sbike() {
        int firstData = 1;
        int lastData = 1000; // 한 페이지당 가져올 이벤트 수
        int maxPage = 3000; // 최대 페이지 수

        while (firstData <= maxPage) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/4d5967564d6f757233354443497375/json/bikeList/" + firstData + "/" + (firstData + lastData - 1);
            RestTemplate restTemplate = new RestTemplate();

            try {
                ResponseEntity<com.sth.sbikeservice.vo.RentBikeStatus> responseEntity = restTemplate.getForEntity(apiUrl, com.sth.sbikeservice.vo.RentBikeStatus.class);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    com.sth.sbikeservice.vo.RentBikeStatus response = responseEntity.getBody();

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

    public void getDistance() {
        // 전체 정류장 정보를 읽어옴
        List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();

        if (!sbikeDTOList.isEmpty()) {
            // 모든 정류장 정보를 사용
            for (SbikeDTO sbikeDTO : sbikeDTOList) {
                // 출발지 고정값
                String origin = "127.11015314141542,37.39472714688412,name=출발";

                // 목적지 정보를 이용하여 Distance 메서드 호출
                String destination = sbikeDTO.getStationLongitude() + "," + sbikeDTO.getStationLatitude();
                Distance(origin, destination, sbikeDTO.getStationName());
            }
        } else {
            System.out.println("No data available in the list.");
        }
    }

    public static void Distance(String origin, String destination, String stationName) {
        try {
            // 카카오디벨로퍼스에서 발급 받은 REST API 키
            String REST_API_KEY = "b607b434b034948b1f4dcba5efc74551";

            // 요청 URL 생성
            String baseUrl = "https://apis-navi.kakaomobility.com/v1/directions";
            String waypoints = "";
            String priority = "RECOMMEND";
            String carFuel = "GASOLINE";
            String carHipass = "false";
            String alternatives = "false";
            String roadDetails = "false";

            StringBuilder queryParams = new StringBuilder();
            queryParams.append("origin=").append(URLEncoder.encode(origin, "UTF-8"));
            queryParams.append("&destination=").append(URLEncoder.encode(destination, "UTF-8"));
            queryParams.append("&waypoints=").append(URLEncoder.encode(waypoints, "UTF-8"));
            queryParams.append("&priority=").append(URLEncoder.encode(priority, "UTF-8"));
            queryParams.append("&car_fuel=").append(URLEncoder.encode(carFuel, "UTF-8"));
            queryParams.append("&car_hipass=").append(URLEncoder.encode(carHipass, "UTF-8"));
            queryParams.append("&alternatives=").append(URLEncoder.encode(alternatives, "UTF-8"));
            queryParams.append("&road_details=").append(URLEncoder.encode(roadDetails, "UTF-8"));

            URL url = new URL(baseUrl + "?" + queryParams.toString());

            // HttpURLConnection 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 메서드 설정 (GET)
            connection.setRequestMethod("GET");

            // 인증 정보 추가
            connection.setRequestProperty("Authorization", "KakaoAK " + REST_API_KEY);

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();

            // 응답이 성공(200)일 경우 데이터 읽기
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // 응답 내용 출력
                    // 일단 주석처리
//                    System.out.println("Response:\n" + response.toString());

                    // JSON 파싱하여 distance 값 추출
                    System.out.println("출발지점 :"+ origin);
                    System.out.println("도착지점 :" +stationName);
                    extractDistance(response.toString());
                }
            } else {
                // 에러 발생 시 에러 코드 출력
                System.out.println("HTTP Request Failed with error code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void extractDistance(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);

            // "routes" 안에 있는 "summary"에서 "distance" 값 추출
            int distance = jsonNode
                    .path("routes")
                    .path(0)
                    .path("summary")
                    .path("distance")
                    .asInt();

            // 추출한 distance 값 출력
            System.out.println("둘 사이 거리: " + distance + " 미터");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}