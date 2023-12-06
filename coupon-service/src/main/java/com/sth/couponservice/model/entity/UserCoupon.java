package com.sth.couponservice.model.entity;

import com.sth.couponservice.model.dto.UserCouponDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponID;
    @ManyToOne
    @JoinColumn(name = "couponID")
    private Coupon coupon;
    private String userID; // 사용자 ID만 저장 (외래 키 관계 없음)
    private boolean isUsed;

    public UserCouponDTO toDTO() {
        return UserCouponDTO.builder()
                .userCouponID(userCouponID)
                .coupon(coupon)
                .userID(userID)
                .isUsed(isUsed)
                .build();
    }
}
