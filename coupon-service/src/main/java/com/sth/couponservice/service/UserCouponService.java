package com.sth.couponservice.service;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.dto.UserCouponDTO;
import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.model.entity.UserCoupon;
import com.sth.couponservice.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;

    @Autowired
    public UserCouponService(UserCouponRepository userCouponRepository, CouponService couponService) {
        this.userCouponRepository = userCouponRepository;
        this.couponService = couponService;
    }

    public void issueCouponsToUser(Long couponId, String userId) {
        CouponDTO couponDTO = couponService.getCouponById(couponId);

        // 이미 발급 받았으면
        if (userCouponRepository.existsByCouponAndUserID(couponDTO.toEntity(), userId)) {
            return;
        }

        UserCoupon userCoupon = UserCoupon.builder()
                .coupon(couponDTO.toEntity())
                .userID(userId)
                .isUsed(false)
                .build();

        userCouponRepository.save(userCoupon);
    }

    public List<UserCouponDTO> listUserCoupon() {
        List<UserCoupon> userCouponList = userCouponRepository.findAll();
        List<UserCouponDTO> userCouponDTOList = new ArrayList<>();
        userCouponList.forEach(userCoupon -> {
            userCouponDTOList.add(userCoupon.toDTO());
        });

        return userCouponDTOList;

    }
}
