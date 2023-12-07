package com.sth.couponservice.model.dto;

import com.sth.couponservice.model.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDTO {
    private Long couponID;
    private String couponCode;
    private int quantity;

    public Coupon toEntity() {
        return Coupon.builder()
                .couponID(couponID)
                .couponCode(couponCode)
                .quantity(quantity)
                .build();
    }
}
