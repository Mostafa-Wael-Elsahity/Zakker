package com.example.elearningplatform.payment.coupon.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateCouponRequest {
    @NotEmpty(message = "Code cannot be empty")
    private String code;
    @NotEmpty(message = "Expiration date cannot be empty")
    private Integer expirationDate;
    @NotEmpty(message = "Number of coupons cannot be empty")
    private Integer numberOfCoupons;
    @NotEmpty(message = "Discount cannot be empty")
    private Integer discount;
    private Integer courseId;
}
