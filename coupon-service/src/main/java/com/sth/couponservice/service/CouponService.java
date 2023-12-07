package com.sth.couponservice.service;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.repository.CouponRepository;
import com.sth.couponservice.vo.CouponException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, Long> redisLongTemplate;

    // 쿠폰 코드를 기반으로 발급 수를 관리하는 Redis 키의 템플릿
    private static final String COUPON_COUNT_KEY_TEMPLATE = "coupon:%s:count";
    private static final long MAX_COUPON_COUNT = 1000; // 최대 발급 가능한 쿠폰 수

    @Autowired
    public CouponService(CouponRepository couponRepository, RedisTemplate<String, Long> redisLongTemplate) {
        this.couponRepository = couponRepository;
        this.redisLongTemplate = redisLongTemplate;
    }

    @Transactional
    public CouponDTO createCoupon(String couponCode, int quantity) {
        if(couponCode == null || quantity < 0) {
            return null;
        }

        // 쿠폰 코드가 존재하면
        if (couponRepository.existsByCouponCode(couponCode)) {
            throw new CouponException("Coupon code already Exists.");
        }

        String couponCountKey = String.format(COUPON_COUNT_KEY_TEMPLATE, couponCode);


        // 생성
        int issuedCouponCount = redisLongTemplate.opsForValue().increment(couponCountKey, quantity).intValue();

        if (issuedCouponCount > MAX_COUPON_COUNT) {
            // 최대 발급 가능한 쿠폰 수 초과
            throw new CouponException("Max coupon count exceeded.");
            // 여기에서 롤백 또는 예외 처리를 수행할 수 있음
//            redisTemplate.opsForValue().increment(couponCountKey, -quantity); // 다시 감소
//            return null;
        }

        Coupon coupon = Coupon.builder()
                .couponCode(couponCode)
                .quantity(quantity)
                .build();

        couponRepository.save(coupon);
        return coupon.toDTO();
    }

    // 쿠폰 발급
    @Transactional
    public void issueCoupon(String couponCode) {
        String couponCountKey = String.format(COUPON_COUNT_KEY_TEMPLATE, couponCode);

        // coupon 체크
        try {
            checkRedis(couponCode, couponCountKey);
            CouponDTO couponDTO = getCouponByCode(couponCode);
            couponDTO.setQuantity(redisLongTemplate.opsForValue().get(couponCountKey).intValue());

            couponRepository.save(couponDTO.toEntity());
        } catch (CouponException e) {
            e.printStackTrace();
        }
    }

    // Id로 쿠폰 조회
    public CouponDTO getCouponById(Long couponId) {
        Optional<Coupon> coupon = couponRepository.findById(couponId);

        if(coupon.isPresent()) {
            return coupon.get().toDTO();
        }

        return null;
    }

    // Code로 쿠폰 조회
    public CouponDTO getCouponByCode(String couponCode) {
        Optional<Coupon> coupon = couponRepository.findByCouponCode(couponCode);

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

    // 쿠폰 체크
    public void checkRedis(String couponCode, String redisKey) {
        Long quantity = redisLongTemplate.opsForValue().get(redisKey);

        if (quantity == null) {
            // Redis에 데이터가 없으면 데이터베이스에서 조회
            CouponDTO couponDTO = getCouponByCode(couponCode);

            if(couponDTO == null) {
                // 데이터베이스에도 없는 경우, 원하는 로직 수행
                throw new CouponException("Coupon not found with Code: " + couponCode);
            } else {
                // 데이터베이스에서 조회한 데이터를 Redis에 저장
                redisLongTemplate.opsForValue().set(redisKey, (long)couponDTO.getQuantity());
            }
        }
    }
}
