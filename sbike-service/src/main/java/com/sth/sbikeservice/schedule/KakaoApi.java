package com.sth.sbikeservice.schedule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@Service
public class KakaoApi {

    public void getDistance(String origin, String destination) {
        try {
            String REST_API_KEY = "b607b434b034948b1f4dcba5efc74551";
            String baseUrl = "https://apis-navi.kakaomobility.com/v1/directions";

            StringBuilder queryParams = new StringBuilder();
            queryParams.append("origin=").append(URLEncoder.encode(origin, "UTF-8"));
            queryParams.append("&destination=").append(URLEncoder.encode(destination, "UTF-8"));
            queryParams.append("&waypoints=").append(URLEncoder.encode("", "UTF-8"));
            queryParams.append("&priority=").append(URLEncoder.encode("RECOMMEND", "UTF-8"));
            queryParams.append("&car_fuel=").append(URLEncoder.encode("GASOLINE", "UTF-8"));
            queryParams.append("&car_hipass=").append(URLEncoder.encode("false", "UTF-8"));
            queryParams.append("&alternatives=").append(URLEncoder.encode("false", "UTF-8"));
            queryParams.append("&road_details=").append(URLEncoder.encode("false", "UTF-8"));

            URL url = new URL(baseUrl + "?" + queryParams.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "KakaoAK " + REST_API_KEY);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    log.info("Response:\n{}", response.toString());
                    extractDistance(response.toString());
                }
            } else {
                log.error("HTTP Request Failed with error code: {}", responseCode);
            }

        } catch (Exception e) {
            log.error("Error during Kakao API call: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void extractDistance(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);

            int distance = jsonNode
                    .path("routes")
                    .path(0)
                    .path("summary")
                    .path("distance")
                    .asInt();

            log.info("Distance: {} meters", distance);
        } catch (Exception e) {
            log.error("Error while extracting distance: {}", e.getMessage());
            e.printStackTrace();
        }

    }
}
