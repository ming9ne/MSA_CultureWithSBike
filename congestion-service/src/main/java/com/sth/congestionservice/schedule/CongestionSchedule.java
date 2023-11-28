package com.sth.congestionservice.schedule;


import com.sth.congestionservice.model.dto.CongestionDTO;
import com.sth.congestionservice.model.dto.PopulationDTO;
import com.sth.congestionservice.service.CongestionService;
import com.sth.congestionservice.service.PopulationService;
import com.sth.congestionservice.vo.AreaResponse;
import com.sth.congestionservice.vo.Citydata;
import com.sth.congestionservice.vo.CongestionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CongestionSchedule {
    private final CongestionService congestionService;
    private final PopulationService populationService;

    @Scheduled(fixedDelay = 300000)
    public void hello() {
       log.info("fixedRate Scheduler");
    }

    @Scheduled(fixedDelay = 300000)
    public void saveCongestions() {
        RestTemplate restTemplate = new RestTemplate();

//        List<String> area = restTemplate.getForObject("http://localhost:8000/api/v1/area-service/areas");
        String areaApiUrl = "http://localhost:8000/api/v1/area-service/areas";


        AreaResponse[] areas = restTemplate.getForObject(areaApiUrl, AreaResponse[].class);

        for(int i = 0; i < areas.length; i++) {
            // API 호출 및 데이터 저장
            int startPage = 1;
            int pageSize = 100; // 한 페이지당 가져올 이벤트 수

            String apiUrl = "http://openapi.seoul.go.kr:8088/48435455656b617238305977625a75/xml/citydata_ppltn/" + startPage + "/" + pageSize + "/" + areas[i].getAreaNm();
//            String apiUrl = "http://openapi.seoul.go.kr:8088/48435455656b617238305977625a75/xml/citydata_ppltn/1/100/광화문·덕수궁";


            try {
                // API 호출 및 응답을 ResponseEntity로 받음
                ResponseEntity<CongestionResponse> responseEntity = restTemplate.getForEntity(apiUrl, CongestionResponse.class);
//                return responseEntity;
                // API 호출이 성공한 경우
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    CongestionResponse response = responseEntity.getBody();

                    // 여기서부터는 이벤트 정보를 처리하면 됩니다
                    if (response != null && response.getRow() != null && !response.getRow().isEmpty()) {
                        List<Citydata> citydataList = response.getRow();
                        List<CongestionDTO> congestionDTOList = new ArrayList<>();
                        List<PopulationDTO> populationDTOList = new ArrayList<>();

                        citydataList.forEach(citydata -> {
                            congestionDTOList.add(CongestionDTO.builder()
                                    .areaNm(citydata.getAREA_NM())
                                    .areaCongestLvl(citydata.getAREA_CONGEST_LVL())
                                    .areaCongestMsg(citydata.getAREA_CONGEST_MSG())
                                    .build());

                            populationDTOList.add(PopulationDTO.builder()
                                    .areaNm(citydata.getAREA_NM())
                                    .areaPpltnMin(citydata.getAREA_PPLTN_MIN())
                                    .areaPpltnMax(citydata.getAREA_PPLTN_MAX())
                                    .build());
                        });

                        congestionService.addCongestions(congestionDTOList);
                        populationService.addPopulations(populationDTOList);

                        System.out.println("API 호출 성공 - 페이지: " + startPage);
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

    }

}