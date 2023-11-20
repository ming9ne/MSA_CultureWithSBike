package com.sth.congestionservice.schedule;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CongestionSchedule {
    @Scheduled(fixedDelay = 300000)
    public void hello() {
       log.info("fixedRate Scheduler");
    }

    public void callAPI() {
        String url = "http://openapi.seoul.go.kr:8088/48435455656b617238305977625a75/xml/citydata_ppltn/1/5/";
    }
}
