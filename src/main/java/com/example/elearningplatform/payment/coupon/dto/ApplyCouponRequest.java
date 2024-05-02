package com.example.elearningplatform.payment.coupon.dto;

import lombok.Data;

@Data
public class ApplyCouponRequest {
      private String couponCode;
      private Integer courseId;
}