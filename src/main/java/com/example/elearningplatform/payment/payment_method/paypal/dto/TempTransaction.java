package com.example.elearningplatform.payment.payment_method.paypal.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"payer_id", "payment_id"}))
public class TempTransaction {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Integer id;

      String paymentId;
      String payerId;
      Integer courseId;
      Integer userId;
      Integer price;
      Integer couponId;
      
}
