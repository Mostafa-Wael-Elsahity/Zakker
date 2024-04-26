package com.example.elearningplatform.payment.payment_method.paypal.dto;

import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;

import lombok.Data;

@Data
public class PaymentPaypalSuccess {
      private String paymentId;
      private String payerId;
      private ApplyCouponRequest applyCouponRequest;
}
