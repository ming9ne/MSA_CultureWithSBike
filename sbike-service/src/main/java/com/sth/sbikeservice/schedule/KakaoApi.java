package com.sth.sbikeservice.schedule;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.KaKaoRepository;
import com.sth.sbikeservice.repository.SbikeRepository;
import com.sth.sbikeservice.service.SbikeService;
import com.sth.sbikeservice.vo.EventResponse;
import com.sth.sbikeservice.vo.RentBikeStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KakaoApi {

    private final SbikeService sbikeService;
    private final KaKaoRepository kaKaoRepository;
    private final RestTemplate restTemplate;
    private final Set<String> alreadyFetchedEvents = new HashSet<>();



    @Autowired
    public KakaoApi(SbikeService sbikeService, KaKaoRepository kaKaoRepository, RestTemplate restTemplate) {
        this.sbikeService = sbikeService;
        this.kaKaoRepository = kaKaoRepository;
        this.restTemplate = restTemplate;

        loadAlreadyFetchedEvents();
    }

//    public void getDistanceAndSaveToDB() {
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            String eventApiUrl = "http://localhost:8000/api/v1/event-service/events";
//            EventResponse[] eventResponses = restTemplate.getForObject(eventApiUrl, EventResponse[].class);
//            List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();
//
//            if (!sbikeDTOList.isEmpty()) {
//                List<KaKao> kaKaoList = new ArrayList<>();
//
//                for (EventResponse eventResponse : eventResponses) {
//                    double eventLongitude = eventResponse.getLot();
//                    double eventLatitude = eventResponse.getLat();
//                    String eventName = eventResponse.getEventNm();
//
//                    String origin = eventLongitude + "," + eventLatitude + ",name=" + eventName;
//
//                    System.out.println("Origin value: " + origin);
//
//                    // 이미 해당 stationName이 DB에 존재하는지 확인
//                    Optional<KaKao> existingKaKaoData = kaKaoRepository.findByStationName(eventName);
//
//                    if (existingKaKaoData.isPresent()) {
//                        // 이미 존재하면 건너뛰고 다음 이벤트로 진행
//                        log.info("이미 존재하는 데이터입니다 ");
//                        System.out.println("이벤트 데이터 이미 존재함. 건너뜀.");
//                        continue;
//                    }
//
//                    // 각 이벤트에 대한 정류장 거리 계산 및 KaKao 엔티티 생성
//                    List<KaKao> eventKaKaoList = new ArrayList<>();
//
//                    for (SbikeDTO sbikeDTO : sbikeDTOList) {
//                        double stationLongitude = Double.parseDouble(sbikeDTO.getStationLongitude());
//                        double stationLatitude = Double.parseDouble(sbikeDTO.getStationLatitude());
//
//                        // 범위 내의 데이터만 처리
//                        if (stationLongitude >= eventLongitude - 0.015 && stationLongitude <= eventLongitude + 0.015
//                                && stationLatitude >= eventLatitude - 0.015 && stationLatitude <= eventLatitude + 0.015) {
//
//                            String destination = stationLongitude + "," + stationLatitude;
//                            String stationName = sbikeDTO.getStationName();
//                            int distance = getDistance(origin, destination);
//
//                            KaKao kaKao = KaKao.builder()
//                                    .stationName(stationName)
//                                    .origin(eventName)
//                                    .destination(destination)
//                                    .distance(distance)
//                                    .stationLatitude(Double.parseDouble(String.valueOf(eventLatitude)))
//                                    .stationLongitude(Double.parseDouble(String.valueOf(eventLongitude)))
//                                    .build();
//
//                            eventKaKaoList.add(kaKao);
//                        }
//                    }
//
//                    // 거리를 기준으로 정렬
//                    eventKaKaoList.sort(Comparator.comparingInt(KaKao::getDistance));
//
//                    // 상위 3개의 데이터만 선택
//                    List<KaKao> selectedKaKaoList = eventKaKaoList.stream().limit(3).collect(Collectors.toList());
//
//                    kaKaoList.addAll(selectedKaKaoList);
//                }
//
//                // 최종 결과를 DB에 저장
//                kaKaoRepository.saveAll(kaKaoList);
//            } else {
//                System.out.println("리스트에 사용 가능한 데이터가 없습니다.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("이벤트 데이터를 가져오거나 API 호출에 실패했습니다.");
//        }
//    }
public void getDistanceAndSaveToDB() {
    try {
        RestTemplate restTemplate = new RestTemplate();
        String eventApiUrl = "http://localhost:8000/api/v1/event-service/events";
        EventResponse[] eventResponses = restTemplate.getForObject(eventApiUrl, EventResponse[].class);
        List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();

        if (!sbikeDTOList.isEmpty()) {
            List<KaKao> kaKaoList = new ArrayList<>();

            for (EventResponse eventResponse : eventResponses) {
                double eventLongitude = eventResponse.getLot();
                double eventLatitude = eventResponse.getLat();
                String eventName = eventResponse.getEventNm();

                String origin = eventLongitude + "," + eventLatitude + ",name=" + eventName;

                System.out.println("Origin value: " + origin);

                // 이미 해당 이벤트가 가져온 이벤트 목록에 있는지 확인
                if (alreadyFetchedEvents.contains(eventName)) {
                    // 이미 가져온 이벤트면 건너뛰고 다음 이벤트로 진행
                    log.info("이미 존재하는 데이터입니다 ");
                    System.out.println("이벤트 데이터 이미 존재함. 건너뜀.");
                    continue;
                }

                // 각 이벤트에 대한 정류장 거리 계산 및 KaKao 엔티티 생성
                List<KaKao> eventKaKaoList = new ArrayList<>();

                for (SbikeDTO sbikeDTO : sbikeDTOList) {
                    double stationLongitude = Double.parseDouble(sbikeDTO.getStationLongitude());
                    double stationLatitude = Double.parseDouble(sbikeDTO.getStationLatitude());

                    // 범위 내의 데이터만 처리
                    if (stationLongitude >= eventLongitude - 0.015 && stationLongitude <= eventLongitude + 0.015
                            && stationLatitude >= eventLatitude - 0.015 && stationLatitude <= eventLatitude + 0.015) {

                        String destination = stationLongitude + "," + stationLatitude;
                        String stationName = sbikeDTO.getStationName();
                        int distance = getDistance(origin, destination);

                        KaKao kaKao = KaKao.builder()
                                .stationName(stationName)
                                .origin(eventName)
                                .destination(destination)
                                .distance(distance)
                                .stationLatitude(Double.parseDouble(String.valueOf(eventLatitude)))
                                .stationLongitude(Double.parseDouble(String.valueOf(eventLongitude)))
                                .build();

                        eventKaKaoList.add(kaKao);
                    }
                }

                // 거리를 기준으로 정렬
                eventKaKaoList.sort(Comparator.comparingInt(KaKao::getDistance));

                // 상위 3개의 데이터만 선택
                List<KaKao> selectedKaKaoList = eventKaKaoList.stream().limit(3).collect(Collectors.toList());

                kaKaoList.addAll(selectedKaKaoList);

                // 이벤트 데이터를 가져왔다고 표시
                alreadyFetchedEvents.add(eventName);
            }

            // 최종 결과를 DB에 저장
            kaKaoRepository.saveAll(kaKaoList);
        } else {
            System.out.println("리스트에 사용 가능한 데이터가 없습니다.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("이벤트 데이터를 가져오거나 API 호출에 실패했습니다.");
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
//                    System.out.println("Response:\n" + response.toString());

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
    @PreDestroy
    public void saveAlreadyFetchedEvents() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("alreadyFetchedEvents.ser"));
            out.writeObject(alreadyFetchedEvents);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 서비스 시작 시 파일에서 데이터를 읽어옴
    private void loadAlreadyFetchedEvents() {
        try {
            File file = new File("alreadyFetchedEvents.ser");
            if (file.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                alreadyFetchedEvents.addAll((Set<String>) in.readObject());
                in.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}