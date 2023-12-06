package com.sth.couponservice.model.entity;

import com.sth.couponservice.model.dto.CouponDTO;
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
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponID;
    private String couponCode;
    private int quantity;

    public CouponDTO toDTO() {
        return CouponDTO.builder()
                .couponID(couponID)
                .couponCode(couponCode)
                .quantity(quantity)
                .build();
    }
}
