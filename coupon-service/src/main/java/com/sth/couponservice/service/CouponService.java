package com.sth.couponservice.service;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.repository.CouponRepository;
import com.sth.couponservice.vo.CouponException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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

    // 쿠폰 생성
    @Transactional
    public CouponDTO createCoupon(String couponName, int quantity) {
        if(couponName == null || quantity < 0) {
            throw new CouponException("Coupon Request Failed.");
        }

        String couponCode;

        do {
            // 12바이트를 생성하여 Base64로 인코딩 (16자리 이상이 될 수 있음)
            byte[] randomBytes = new byte[12];
            new SecureRandom().nextBytes(randomBytes);

            // Base64 인코딩 후 16자리로 자르기
            couponCode = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        } while(couponRepository.existsByCouponCode(couponCode));

        String couponCountKey = String.format(COUPON_COUNT_KEY_TEMPLATE, couponCode);

        // 레디스 동시성 체크
        int issuedCouponCount = redisLongTemplate.opsForValue().increment(couponCountKey, quantity).intValue();

        // 최대 발급 가능한 쿠폰 수 초과
        if (issuedCouponCount > MAX_COUPON_COUNT) {
            // 롤백
            redisLongTemplate.opsForValue().increment(couponCountKey, -quantity).intValue();
            throw new CouponException("Max coupon count exceeded.");
        }

        Coupon coupon = Coupon.builder()
                .couponCode(couponCode)
                .couponName(couponName)
                .quantity(quantity)
                .issueDate(LocalDate.now())
                .expirationDate(LocalDate.now().plus(1, ChronoUnit.WEEKS))
                .build();

        couponRepository.save(coupon);
        return coupon.toDTO();
    }

    // 쿠폰 생성 만료일 지정
    @Transactional
    public CouponDTO createCoupon(String couponName, int quantity, LocalDate expirationDate) {
        if(couponName == null || quantity < 0) {
            throw new CouponException("Coupon Request Failed.");
        }

        String couponCode;

        do {
            // 12바이트를 생성하여 Base64로 인코딩 (16자리 이상이 될 수 있음)
            byte[] randomBytes = new byte[12];
            new SecureRandom().nextBytes(randomBytes);

            // Base64 인코딩 후 16자리로 자르기
            couponCode = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        } while(couponRepository.existsByCouponCode(couponCode));

        String couponCountKey = String.format(COUPON_COUNT_KEY_TEMPLATE, couponCode);

        // 레디스 동시성 체크
        int issuedCouponCount = redisLongTemplate.opsForValue().increment(couponCountKey, quantity).intValue();

        // 최대 발급 가능한 쿠폰 수 초과
        if (issuedCouponCount > MAX_COUPON_COUNT) {
            // 롤백
            redisLongTemplate.opsForValue().increment(couponCountKey, -quantity).intValue();
            throw new CouponException("Max coupon count exceeded.");
        }

        Coupon coupon = Coupon.builder()
                .couponCode(couponCode)
                .couponName(couponName)
                .quantity(quantity)
                .issueDate(LocalDate.now())
                .expirationDate(expirationDate)
                .build();

        couponRepository.save(coupon);
        return coupon.toDTO();
    }

    // 쿠폰 발급
    @Transactional
    public CouponDTO issueCoupon(String couponCode, int issuedCouponCount) {
        String couponCountKey = String.format(COUPON_COUNT_KEY_TEMPLATE, couponCode);

        // coupon 체크
        try {
            checkRedis(couponCode, couponCountKey);
            CouponDTO couponDTO = getCouponByCode(couponCode);
            couponDTO.setQuantity(issuedCouponCount);

            couponRepository.save(couponDTO.toEntity());

            return couponDTO;
        } catch (CouponException e) {
            throw new CouponException("Coupon does not exists");
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
