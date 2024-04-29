package com.example.elearningplatform.payment.coupon;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.payment.coupon.dto.CreateCouponRequest;
import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CourseRepository courseRepository;

    /******************************************************************************************** */
    public Response createCoupon(CreateCouponRequest request) {
        try {

            List<Coupon> coupons = couponRepository.findByCourseId(request.getCourseId());
            for (Coupon coupon : coupons) {
                if (coupon.getCode().equals(request.getCode())) {
                    return new Response(HttpStatus.BAD_REQUEST, "coupon code already exists", null);

                }
            }

            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new Exception("Course not found"));

            Coupon coupon = new Coupon();
            coupon.setExpirationDate(LocalDateTime.now().plusDays(request.getExpirationDate()));
            coupon.setNumberOfCoupons(request.getNumberOfCoupons());
            coupon.setDiscount(request.getDiscount());
            coupon.setCourse(course);
            coupon.setCode(request.getCode());
            couponRepository.save(coupon);

            return new Response(HttpStatus.OK, "coupon created successfully", coupon);
        } catch (

        Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /******************************************************************************************** */
    public Response deleteCoupon(Integer couponId) {
        try {
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new Exception("coupon not found"));
            couponRepository.delete(coupon);
            return new Response(HttpStatus.OK, "coupon deleted successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /******************************************************************************************** */

}
