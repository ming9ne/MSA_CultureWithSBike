package com.sth.couponservice.vo;

import lombok.Data;

@Data
public class RequestCoupon {
    String couponCode;
    private int quantity;
}
