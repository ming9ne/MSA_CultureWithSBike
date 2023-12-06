package com.sth.couponservice.controller;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.dto.UserCouponDTO;
import com.sth.couponservice.service.CouponService;
import com.sth.couponservice.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon-service")
public class CouponController {
    private final CouponService couponService;
    private final UserCouponService userCouponService;

    @Autowired
    public CouponController(CouponService couponService, UserCouponService userCouponService) {
        this.couponService = couponService;
        this.userCouponService = userCouponService;
    }

    
    // 쿠폰 리스트 조회
    @GetMapping("/coupons")
    public List<CouponDTO> getCoupons() {
        return couponService.listCoupon();
    }

    // 쿠폰 등록
    @PostMapping("/coupons")
    public void createCoupon() {

    }

    // 유저 쿠폰 리스트 조회
    @GetMapping("/userCoupons")
    public List<UserCouponDTO> getUserCoupons() {
        return userCouponService.listUserCoupon();
    }
}
