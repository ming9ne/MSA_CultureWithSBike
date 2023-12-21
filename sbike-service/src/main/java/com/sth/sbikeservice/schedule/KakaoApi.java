package com.sth.sbikeservice.schedule;

//import com.ctc.wstx.shaded.msv_core.datatype.xsd.Comparator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.KaKaoRepository;
import com.sth.sbikeservice.service.KakaoService;
import com.sth.sbikeservice.service.SbikeService;
import com.sth.sbikeservice.vo.NearSbike;
import com.sth.sbikeservice.vo.ResponseEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KakaoApi {


    private final KakaoService kakaoService;

    @Autowired
    public KakaoApi(KakaoService kakaoService) {

        this.kakaoService = kakaoService;

    }

    @Scheduled(cron = "0 0 */12 * * *") // 12시간마다 실행

    @CircuitBreaker(name = "basicCircuitBreaker", fallbackMethod = "fallbackForCreateKakao")
    public void createKakao() {
        try {
            kakaoService.createKakao();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("이벤트 데이터를 가져오거나 API 호출에 실패했습니다.");
        }
    }

}