package com.sth.couponservice.model.entity;

import com.sth.couponservice.model.dto.CouponDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;
    @Column(nullable = false, unique = true)
    private String couponCode;
    @Column(nullable = false)
    private String couponName;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private LocalDate issueDate;  // 발급 일자
    @Column(nullable = false)
    private LocalDate expirationDate;  // 만료 기간

    public CouponDTO toDTO() {
        return CouponDTO.builder()
                .couponId(couponId)
                .couponCode(couponCode)
                .couponName(couponName)
                .quantity(quantity)
                .issueDate(issueDate)
                .expirationDate(expirationDate)
                .build();
    }
}
