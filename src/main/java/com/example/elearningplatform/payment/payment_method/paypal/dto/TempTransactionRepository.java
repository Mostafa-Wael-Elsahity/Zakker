package com.example.elearningplatform.payment.payment_method.paypal.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TempTransactionRepository extends JpaRepository<TempTransaction, Integer> {

      TempTransaction findByPaymentIdAndPayerId(String paymentId, String payerId);

      void deleteByPaymentIdAndPayerId(String paymentId, String payerId);

}
