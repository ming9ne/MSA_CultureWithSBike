package com.sth.couponservice.service;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.dto.UserCouponDTO;
import com.sth.couponservice.model.entity.UserCoupon;
import com.sth.couponservice.repository.UserCouponRepository;
import com.sth.couponservice.vo.CouponException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;
    private RedisTemplate<String, Long> redisLongTemplate;

    // 쿠폰 코드를 기반으로 발급 수를 관리하는 Redis 키의 템플릿
    private static final String COUPON_COUNT_KEY_TEMPLATE = "coupon:%s:count";

    @Autowired
    public UserCouponService(UserCouponRepository userCouponRepository, CouponService couponService, RedisTemplate<String, Long> redisLongTemplate) {
        this.userCouponRepository = userCouponRepository;
        this.couponService = couponService;
        this.redisLongTemplate = redisLongTemplate;
    }

    // 쿠폰 발급
    @Transactional
    public UserCouponDTO issueCouponsToUser(String couponCode, String userId) {
        CouponDTO couponDTO = couponService.getCouponByCode(couponCode);
        
        // userId 체크 하는 기능

        // 이미 발급 받았으면
        if (userCouponRepository.existsByCouponAndUserId(couponDTO.toEntity(), userId)) {
            throw new CouponException("You have already claimed this coupon. Duplicate issuance is not allowed.");
        }

        String couponCountKey = String.format(COUPON_COUNT_KEY_TEMPLATE, couponDTO.getCouponCode());

        // 쿠폰 체크
        try {
            checkRedis(couponCode, couponCountKey);
        } catch (CouponException e) {
            throw new CouponException("Coupon does not exists");
        }

        // 선착순 발급
        int issuedCouponCount = redisLongTemplate.opsForValue().increment(couponCountKey, -1L).intValue();

        // 쿠폰 발급 수 감소 후 음수가 되면 예외 처리
        if (issuedCouponCount < 0) {
            // 롤백
            redisLongTemplate.opsForValue().increment(couponCountKey, 1); // 다시 증가
            throw new CouponException("Failed to issue coupon.");
        }

        // 쿠폰 발급
        couponDTO = couponService.issueCoupon(couponCode, issuedCouponCount);

        // 유저 쿠폰 생성
        UserCoupon userCoupon = UserCoupon.builder()
                .coupon(couponDTO.toEntity())
                .userId(userId)
                .isUsed(false)
                .issueDate(LocalDate.now())
                .expirationDate(couponDTO.getExpirationDate())
                .build();

        userCouponRepository.save(userCoupon);
        return userCoupon.toDTO();
    }

    // 쿠폰 전체 조회
    public List<UserCouponDTO> listUserCoupon() {
        List<UserCoupon> userCouponList = userCouponRepository.findAll();
        List<UserCouponDTO> userCouponDTOList = new ArrayList<>();
        userCouponList.forEach(userCoupon -> {
            userCouponDTOList.add(userCoupon.toDTO());
        });

        return userCouponDTOList;
    }

    // 쿠폰 체크
    public void checkRedis(String couponCode, String redisKey) {
        Long quantity = redisLongTemplate.opsForValue().get(redisKey);

        if (quantity == null) {
            // Redis에 데이터가 없으면 데이터베이스에서 조회
            CouponDTO couponDTO = couponService.getCouponByCode(couponCode);

            if(couponDTO == null) {
                // 데이터베이스에도 없는 경우, 원하는 로직 수행
                throw new CouponException("Coupon not found with Code: " + couponCode);
            } else {
                // 데이터베이스에서 조회한 데이터를 Redis에 저장
                redisLongTemplate.opsForValue().set(redisKey, (long)couponDTO.getQuantity());
            }
        }
    }

    // 해당 유저의 쿠폰 조회
    public List<UserCouponDTO> getUserCoupons(String userId) {
        List<UserCoupon> userCouponList = userCouponRepository.findAllByUserId(userId);
        List<UserCouponDTO> userCouponDTOList = new ArrayList<>();

        userCouponList.forEach(userCoupon -> {
            userCouponDTOList.add(userCoupon.toDTO());
        });

        return userCouponDTOList;
    }
}
