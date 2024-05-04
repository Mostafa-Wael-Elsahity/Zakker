package com.example.elearningplatform.payment.paypal.transactions;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "payer_id", "payment_id" }))
public class TempTransactionUser {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Integer id;
      private Boolean confirmed;
      private LocalDateTime confirmDate;
      private String paymentId;
      // private String payerId;
      private String currency;
      private Integer courseId;
      private Integer userId;
      private Integer price;
      private Integer couponId;
      private String paymentMethod;
}