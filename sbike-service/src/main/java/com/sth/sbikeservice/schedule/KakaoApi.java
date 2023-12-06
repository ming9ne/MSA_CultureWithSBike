package com.sth.sbikeservice.schedule;

//import com.ctc.wstx.shaded.msv_core.datatype.xsd.Comparator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.KaKaoRepository;
import com.sth.sbikeservice.repository.SbikeRepository;
import com.sth.sbikeservice.service.SbikeService;
import com.sth.sbikeservice.vo.RentBikeStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KakaoApi {

    private final SbikeService sbikeService;
    private final KaKaoRepository kaKaoRepository;
    @Autowired
    public KakaoApi(SbikeService sbikeService, KaKaoRepository kaKaoRepository) {
        this.sbikeService = sbikeService;

        this.kaKaoRepository = kaKaoRepository;
    }

// 가까운 3정거장 분류 전 DB 저장 코드
//    public void getDistanceAndSaveToDB() {
//        List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();
//
//        if (!sbikeDTOList.isEmpty()) {
//            for (SbikeDTO sbikeDTO : sbikeDTOList) {
//                String origin = "127.11015314141542,37.39472714688412,name=출발";
//                String destination = sbikeDTO.getStationLongitude() + "," + sbikeDTO.getStationLatitude();
//                String stationName = sbikeDTO.getStationName();
//
//                // Distance 메서드 호출 및 거리 추출
//                int distance = getDistance(origin, destination);
//
//                // KaKao 엔티티 생성 및 저장
//                KaKao kaKao = KaKao.builder()
//                        .stationName(stationName)
//                        .origin(origin)
//                        .destination(destination)
//                        .distance(distance)
//                        .stationLatitude(sbikeDTO.getStationLatitude())
//                        .stationLongitude(sbikeDTO.getStationLongitude())
//                        .build();
//
//                kaKaoRepository.save(kaKao);
//            }
//        } else {
//            System.out.println("No data available in the list.");
//        }
//    }

    public void getDistanceAndSaveToDB() {
        List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();

        if (!sbikeDTOList.isEmpty()) {
            List<KaKao> kaKaoList = new ArrayList<>();

            // 각 정류장의 거리를 계산하여 KaKao 엔티티 생성
            for (SbikeDTO sbikeDTO : sbikeDTOList) {
                String origin = "127.11015314141542,37.39472714688412,name=문화행사명";
                String destination = sbikeDTO.getStationLongitude() + "," + sbikeDTO.getStationLatitude();
                String stationName = sbikeDTO.getStationName();

                // Distance 메서드 호출 및 거리 추출
                int distance = getDistance(origin, destination);

                // KaKao 엔티티 생성
                KaKao kaKao = KaKao.builder()
                        .stationName(stationName)
                        .origin(origin)
                        .destination(destination)
                        .distance(distance)
                        .stationLatitude(sbikeDTO.getStationLatitude())
                        .stationLongitude(sbikeDTO.getStationLongitude())
                        .build();

                kaKaoList.add(kaKao);
            }

            // 거리를 기준으로 정렬
            kaKaoList.sort(Comparator.comparingInt(KaKao::getDistance));

            // 상위 3개의 데이터만 선택
            List<KaKao> selectedKaKaoList = kaKaoList.stream().limit(3).collect(Collectors.toList());

            // 선택된 데이터를 DB에 저장
            kaKaoRepository.saveAll(selectedKaKaoList);
        } else {
            System.out.println("No data available in the list.");
        }
    }



    public int getDistance(String origin, String destination) {
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
                    System.out.println("Response:\n" + response.toString());

                    // JSON 파싱하여 distance 값 추출
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(response.toString());
                    int distance = jsonNode
                            .path("routes")
                            .path(0)
                            .path("summary")
                            .path("distance")
                            .asInt();

                    // 추출한 distance 값 출력
                    System.out.println("둘 사이 거리: " + distance + " 미터");

                    // 추출한 distance 값 반환
                    return distance;
                }
            } else {
                // 에러 발생 시 에러 코드 출력
                System.out.println("HTTP Request Failed with error code: " + responseCode);
                return -1; // 에러 발생 시 -1 반환 또는 다른 방식으로 처리
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 예외 발생 시 -1 반환 또는 다른 방식으로 처리
        }
    }


//    public void getDistance() {
//        // 전체 정류장 정보를 읽어옴
//        List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();
//
//        if (!sbikeDTOList.isEmpty()) {
//            // 모든 정류장 정보를 사용
//            for (SbikeDTO sbikeDTO : sbikeDTOList) {
//                // 출발지 고정값
//                String origin = "127.11015314141542,37.39472714688412,name=출발";
//
//                // 목적지 정보를 이용하여 Distance 메서드 호출
//                String destination = sbikeDTO.getStationLongitude() + "," + sbikeDTO.getStationLatitude();
//                Distance(origin, destination, sbikeDTO.getStationName());
//            }
//        } else {
//            System.out.println("No data available in the list.");
//        }
//    }

//    public static void Distance(String origin, String destination, String stationName) {
//        try {
//            // 카카오디벨로퍼스에서 발급 받은 REST API 키
//            String REST_API_KEY = "b607b434b034948b1f4dcba5efc74551";
//
//            // 요청 URL 생성
//            String baseUrl = "https://apis-navi.kakaomobility.com/v1/directions";
//            String waypoints = "";
//            String priority = "RECOMMEND";
//            String carFuel = "GASOLINE";
//            String carHipass = "false";
//            String alternatives = "false";
//            String roadDetails = "false";
//
//            StringBuilder queryParams = new StringBuilder();
//            queryParams.append("origin=").append(URLEncoder.encode(origin, "UTF-8"));
//            queryParams.append("&destination=").append(URLEncoder.encode(destination, "UTF-8"));
//            queryParams.append("&waypoints=").append(URLEncoder.encode(waypoints, "UTF-8"));
//            queryParams.append("&priority=").append(URLEncoder.encode(priority, "UTF-8"));
//            queryParams.append("&car_fuel=").append(URLEncoder.encode(carFuel, "UTF-8"));
//            queryParams.append("&car_hipass=").append(URLEncoder.encode(carHipass, "UTF-8"));
//            queryParams.append("&alternatives=").append(URLEncoder.encode(alternatives, "UTF-8"));
//            queryParams.append("&road_details=").append(URLEncoder.encode(roadDetails, "UTF-8"));
//
//            URL url = new URL(baseUrl + "?" + queryParams.toString());
//
//            // HttpURLConnection 생성
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            // 요청 메서드 설정 (GET)
//            connection.setRequestMethod("GET");
//
//            // 인증 정보 추가
//            connection.setRequestProperty("Authorization", "KakaoAK " + REST_API_KEY);
//
//            // 응답 코드 확인
//            int responseCode = connection.getResponseCode();
//
//            // 응답이 성공(200)일 경우 데이터 읽기
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String inputLine;
//                    StringBuilder response = new StringBuilder();
//
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//
//                    // 응답 내용 출력
//                    // 일단 주석처리
////                    System.out.println("Response:\n" + response.toString());
//
//                    // JSON 파싱하여 distance 값 추출
//                    System.out.println("출발지점 :"+ origin);
//                    System.out.println("도착지점 :" +stationName);
//                    extractDistance(response.toString());
//                }
//            } else {
//                // 에러 발생 시 에러 코드 출력
//                System.out.println("HTTP Request Failed with error code: " + responseCode);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void extractDistance(String jsonData) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(jsonData);
//
//            // "routes" 안에 있는 "summary"에서 "distance" 값 추출
//            int distance = jsonNode
//                    .path("routes")
//                    .path(0)
//                    .path("summary")
//                    .path("distance")
//                    .asInt();
//
//            // 추출한 distance 값 출력
//            System.out.println("둘 사이 거리: " + distance + " 미터");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}