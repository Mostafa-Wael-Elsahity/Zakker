package com.example.elearningplatform.payment.coupon.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CouponDto {
    private String code;
    private LocalDateTime expirationDate;
    private Integer numberOfCoupons;
    public CouponDto(String code, LocalDateTime expirationDate, Integer numberOfCoupons) {
        this.code = code;
        this.expirationDate = expirationDate;
        this.numberOfCoupons = numberOfCoupons;
    }

}
