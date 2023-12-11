package com.sth.couponservice.model.dto;

import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.model.entity.UserCoupon;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponDTO {
    private Long userCouponId;
    private Coupon coupon;
    private String userId; // 사용자 ID만 저장 (외래 키 관계 없음)
    private boolean isUsed;
    private LocalDate issueDate;  // 발급 일자
    private LocalDate expirationDate;  // 만료 기간

    public UserCoupon toEntity() {
        return UserCoupon.builder()
                .userCouponId(userCouponId)
                .coupon(coupon)
                .userId(userId)
                .isUsed(isUsed)
                .issueDate(issueDate)
                .expirationDate(expirationDate)
                .build();
    }
}
