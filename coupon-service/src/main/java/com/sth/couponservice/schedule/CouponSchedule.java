package com.sth.couponservice.schedule;

import com.sth.couponservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponSchedule {
    private final CouponService couponService;

    @Scheduled(fixedDelay = 300000)
    public void hello() {
        log.info("fixedRate Scheduler");
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 12시
    public void CreateCoupon() {
        // 12바이트를 생성하여 Base64로 인코딩 (16자리 이상이 될 수 있음)
        byte[] randomBytes = new byte[12];
        new SecureRandom().nextBytes(randomBytes);

        // Base64 인코딩 후 16자리로 자르기
        String couponCode = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        int quantity = 1000;

        couponService.createCoupon(couponCode, quantity);
    }
}
