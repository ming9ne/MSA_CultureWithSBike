package com.sth.couponservice.model.dto;

import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.model.entity.UserCoupon;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponDTO {
    private Long userCouponID;
    private Coupon coupon;
    private String userID; // 사용자 ID만 저장 (외래 키 관계 없음)
    private boolean isUsed;

    public UserCoupon toEntity() {
        return UserCoupon.builder()
                .userCouponID(userCouponID)
                .coupon(coupon)
                .userID(userID)
                .isUsed(isUsed)
                .build();
    }
}
