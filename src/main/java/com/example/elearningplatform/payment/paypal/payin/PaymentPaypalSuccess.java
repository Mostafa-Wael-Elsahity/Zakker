package com.example.elearningplatform.payment.paypal.payin;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;

import lombok.Data;

@Data
public class PaymentPaypalSuccess {
      private String paymentId;
      private String payerId;
      private ApplyCouponRequest applyCouponRequest;
}