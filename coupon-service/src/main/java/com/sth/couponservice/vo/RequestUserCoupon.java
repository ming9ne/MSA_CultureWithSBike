package com.sth.couponservice.vo;

import lombok.Data;

@Data
public class RequestUserCoupon {
    private String couponCode;
    private String userId;
}
