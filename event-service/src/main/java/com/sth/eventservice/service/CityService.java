package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.CityDTO;
import com.sth.eventservice.model.entity.City;
import com.sth.eventservice.repository.CityRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Data
    @XmlRootElement(name = "SeoulRtd.citydata")  // Updated root element name
    public static class CityListResponse {
        private List<CityDTO> CITYDATA;  // Updated field name

        @XmlElement(name = "CITYDATA")  // Updated element name
        public void setCityData(List<CityDTO> CITYDATA) {
            this.CITYDATA = CITYDATA;
        }
    }

    public void addCityFromApi() {
        String apiUrl = "http://openapi.seoul.go.kr:8088/636f62694e6f757236364b49674759/xml/citydata/1/5/광화문·덕수궁";
        RestTemplate restTemplate = new RestTemplate();

        try {
            // API 호출 및 응답을 ResponseEntity로 받음
            ResponseEntity<CityListResponse> responseEntity = restTemplate.getForEntity(apiUrl, CityListResponse.class);

            // API 호출이 성공한 경우
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                CityListResponse response = responseEntity.getBody();

                // 여기서부터는 이벤트 정보를 처리하면 됩니다
                if (response != null && response.getCITYDATA() != null && !response.getCITYDATA().isEmpty()) {
                    List<CityDTO> cityDTOList = response.getCITYDATA();
                    saveCitiesToDatabase(cityDTOList);
                    System.out.println("API 호출 성공");
                } else {
                    System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다");
                }
            } else {
                // API 호출이 실패한 경우
                System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            System.out.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스 출력
        }
    }

    @Transactional
    public void saveCitiesToDatabase(List<CityDTO> cityDTOList) {
        // DTO를 Entity로 변환하여 저장
        for (CityDTO cityDTO : cityDTOList) {
            City city = cityDTO.toEntity();
            cityRepository.save(city);
        }
    }
}
