package com.example.elearningplatform.payment.paypal.transactions;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TempTransactionUserRepository extends JpaRepository<TempTransactionUser, Integer> {


      Optional<TempTransactionUser> findByPaymentIdAndPayerId(String paymentId, String payerId);

      Optional<TempTransactionUser> findByUserIdAndCourseId(Integer userId, Integer courseId);

      void deleteByPaymentIdAndPayerId(String paymentId, String payerId);
}