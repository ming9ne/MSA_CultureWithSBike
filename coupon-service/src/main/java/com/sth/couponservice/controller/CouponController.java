package com.sth.couponservice.controller;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.dto.UserCouponDTO;
import com.sth.couponservice.service.CouponService;
import com.sth.couponservice.service.UserCouponService;
import com.sth.couponservice.vo.CouponException;
import com.sth.couponservice.vo.RequestCoupon;
import com.sth.couponservice.vo.RequestUserCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/")
    public String hello() {
        return "This is Coupon Service!!";
    }
    
    // 쿠폰 리스트 조회
    @GetMapping("/coupons")
    public List<CouponDTO> listCoupons() {
        return couponService.listCoupon();
    }

    // 쿠폰 생성
    @PostMapping("/coupon")
    public ResponseEntity<Object> createCoupon(@RequestBody RequestCoupon requestCoupon) {
        try {
            CouponDTO couponDTO;
            // 만료일 여부 체크
            if(requestCoupon.getExpirationDate() == null) {
                couponDTO = couponService.createCoupon(requestCoupon.getCouponName(), requestCoupon.getQuantity());
            } else {
                couponDTO = couponService.createCoupon(requestCoupon.getCouponName(), requestCoupon.getQuantity(), requestCoupon.getExpirationDate());
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(couponDTO);
        } catch (CouponException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 유저 쿠폰 리스트 조회
    @GetMapping("/userCoupons")
    public List<UserCouponDTO> listUserCoupons() {
        return userCouponService.listUserCoupon();
    }

    // 해당 유저의 쿠폰 조회
    @GetMapping("/userCoupons/{userId}")
    public List<UserCouponDTO> findUserCoupons(@PathVariable String userId) {
        return userCouponService.getUserCoupons(userId);
    }

    // 쿠폰 발급
    @PostMapping("/userCoupon")
    public ResponseEntity<Object> createUserCoupon(@RequestBody RequestUserCoupon requestUserCoupon) {
        try {
            UserCouponDTO userCouponDTO = userCouponService.issueCouponsToUser(requestUserCoupon.getCouponCode(), requestUserCoupon.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(userCouponDTO);
        } catch(CouponException e) {
            System.out.println("bad");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 발급된 쿠폰 통계
    @GetMapping("/statistics")
    public ResponseEntity<Object> statistics() {
        HashMap<LocalDate, Integer> map = userCouponService.countUserCoupons(LocalDate.now());

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
