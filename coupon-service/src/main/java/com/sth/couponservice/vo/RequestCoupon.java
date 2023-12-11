package com.sth.couponservice.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestCoupon {
    String couponName;
    private int quantity;
    LocalDate expirationDate;
}
