package com.example.elearningplatform.payment.coupon;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findByCourseId(Integer courseId);

    List<Coupon> findByCode(String code);

    Optional<Coupon> findByCodeAndCourseId(String code, Integer courseId);

    void deleteByExpirationDateBefore(LocalDateTime now);

}