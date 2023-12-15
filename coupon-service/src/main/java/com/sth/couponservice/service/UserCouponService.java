package com.sth.couponservice.service;

import com.sth.couponservice.model.dto.CouponDTO;
import com.sth.couponservice.model.dto.UserCouponDTO;
import com.sth.couponservice.model.entity.UserCoupon;
import com.sth.couponservice.repository.UserCouponRepository;
import com.sth.couponservice.vo.CouponException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;
    private RedisTemplate<String, Long> redisLongTemplate;
    private Environment env;

    // 쿠폰 코드를 기반으로 발급 수를 관리하는 Redis 키의 템플릿
    private static final String COUPON_COUNT_KEY_TEMPLATE = "coupon:%s:count";

    @Autowired
    public UserCouponService(UserCouponRepository userCouponRepository, CouponService couponService, RedisTemplate<String, Long> redisLongTemplate, Environment env) {
        this.userCouponRepository = userCouponRepository;
        this.couponService = couponService;
        this.redisLongTemplate = redisLongTemplate;
        this.env = env;
    }

    // 쿠폰 발급
    @Transactional
    public UserCouponDTO issueCouponsToUser(String couponCode, String userId) {
        // userId 체크 하는 기능
        try {
            checkUserId(userId);
        } catch (CouponException e) {
            throw new CouponException(e.getMessage());
        }

        String couponCountKey = String.format(COUPON_COUNT_KEY_TEMPLATE, couponCode);

        // 쿠폰 체크
        try {
            checkRedis(couponCode, couponCountKey);
        } catch (CouponException e) {
            throw new CouponException("Coupon does not exists");
        }

        CouponDTO couponDTO = couponService.getCouponByCode(couponCode);

        // 이미 발급 받았으면
        if (userCouponRepository.existsByCouponAndUserId(couponDTO.toEntity(), userId)) {
            throw new CouponException("You have already claimed this coupon. Duplicate issuance is not allowed.");
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

    // 유저 체크
    public void checkUserId(String userId) {
        RestTemplate restTemplate = new RestTemplate();

        // 유저 조회 API 엔드포인트 URL
        String userApiUrl = "http://" + env.getProperty("gateway") + ":8000/api/v1/user-service/user";
        try {
            // 유저 조회 API 호출
            ResponseEntity<Map> response = restTemplate.getForEntity(userApiUrl + "?id=" + userId, Map.class);

            // 응답에서 데이터를 가져올 수 있는지 확인
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new CouponException("유저 조회에 실패하였습니다. HTTP 상태코드: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CouponException("유저가 존재하지 않습니다.");
            } else {
                throw new CouponException("유저 조회에 실패하였습니다. HTTP 상태코드: " + e.getStatusCode());
            }
        }
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
    
    // 일별 발급된 쿠폰 수 조회
    public HashMap<LocalDate, Integer> countUserCoupons(LocalDate date) {
        HashMap<LocalDate, Integer> map = new HashMap<>();
        for(int i = 0; i < 7; i++) {
            map.put(date.minusDays(i), userCouponRepository.countAllByIssueDate(date.minusDays(i)));
        }
        return map;
    }
}
