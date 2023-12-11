package com.sth.couponservice.model.dto;

import com.sth.couponservice.model.entity.Coupon;
import jakarta.persistence.Column;
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
public class CouponDTO {
    private Long couponId;
    private String couponCode;
    private String couponName;
    private int quantity;
    private LocalDate issueDate;  // 발급 일자
    private LocalDate expirationDate;  // 만료 기간

    public Coupon toEntity() {
        return Coupon.builder()
                .couponId(couponId)
                .couponCode(couponCode)
                .couponName(couponName)
                .quantity(quantity)
                .issueDate(issueDate)
                .expirationDate(expirationDate)
                .build();
    }
}
