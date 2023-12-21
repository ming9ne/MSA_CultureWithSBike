package com.sth.sbikeservice.schedule;


import com.sth.sbikeservice.service.SbikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SbikeSchedule {

    private final SbikeService sbikeService;

    @Autowired
    public SbikeSchedule(SbikeService sbikeService) {
        this.sbikeService = sbikeService;
    }


}