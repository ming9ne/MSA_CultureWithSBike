package com.sth.couponservice.model.entity;

import com.sth.couponservice.model.dto.UserCouponDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponId;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
    @Column(nullable = false)
    private String userId; // 사용자 ID만 저장 (외래 키 관계 없음)
    @Column(columnDefinition = "boolean default false")
    private boolean isUsed;
    @Column(nullable = false)
    private LocalDate issueDate;  // 발급 일자
    @Column(nullable = false)
    private LocalDate expirationDate;  // 만료 기간

    public UserCouponDTO toDTO() {
        return UserCouponDTO.builder()
                .userCouponId(userCouponId)
                .coupon(coupon)
                .userId(userId)
                .isUsed(isUsed)
                .issueDate(issueDate)
                .expirationDate(expirationDate)
                .build();
    }
}
