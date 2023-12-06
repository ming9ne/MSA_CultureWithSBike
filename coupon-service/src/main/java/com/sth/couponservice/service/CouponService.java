package com.sth.couponservice.service;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void createCoupon(String couponCode, int quantity, String eventName) {
        // 쿠폰 코드가 존재하면
        if (couponRepository.existsByCouponCode(couponCode)) {
            return;
        }

        Coupon coupon = Coupon.builder()
                .couponCode(couponCode)
                .quantity(quantity)
                .build();

        couponRepository.save(coupon);
    }

    public CouponDTO getCouponById(Long couponId) {
        Optional<Coupon> coupon = couponRepository.findById(couponId);

        if(coupon.isPresent()) {
            return coupon.get().toDTO();
        }

        return null;
    }

    public List<CouponDTO> listCoupon() {
        List<Coupon> couponList = couponRepository.findAll();
        List<CouponDTO> couponDTOList = new ArrayList<>();
        couponList.forEach(coupon -> {
            couponDTOList.add(coupon.toDTO());
        });

        return couponDTOList;
    }
}
