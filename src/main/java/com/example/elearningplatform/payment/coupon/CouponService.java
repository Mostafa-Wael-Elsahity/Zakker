package com.example.elearningplatform.payment.coupon;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.coupon.dto.CouponDto;
import com.example.elearningplatform.payment.coupon.dto.CreateRequest;
import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CourseRepository courseRepository;

    /******************************************************************************************** */
    public Response createCoupon(CreateRequest request) {
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

            return new Response(HttpStatus.OK, "coupon created successfully",
                    new CouponDto(coupon.getCode(), coupon.getExpirationDate(), coupon.getNumberOfCoupons()));
        } catch (Exception e) {
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

    public Response applyCoupon(ApplyCouponRequest applyCouponRequest) {
        try {
            Course course = courseRepository.findById(applyCouponRequest.getCourseId())
                    .orElseThrow(() -> new Exception("course not found"));
            if (applyCouponRequest.getCouponCode() == null) {
                return new Response(HttpStatus.BAD_REQUEST, "no coupon applied", course.getPrice());
            }
            Integer courseId = applyCouponRequest.getCourseId();
            String coupon = applyCouponRequest.getCouponCode();
            Coupon couponDB = couponRepository.findByCodeAndCourseId(coupon, courseId)
                    .orElseThrow(() -> new Exception("coupon not found"));
            if (couponDB.getExpirationDate().isBefore(LocalDateTime.now())) {
                return new Response(HttpStatus.BAD_REQUEST, "coupon expired", null);
            }
            Double newPrice = course.getPrice()
                    - (couponDB.getDiscount() / 100.0) * course.getPrice();
            return new Response(HttpStatus.OK, "coupon applied successfully", newPrice);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    public void decrementCoupon(Integer couponId) throws Exception {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new Exception("coupon not found"));
        Integer numberOfCoupons = coupon.getNumberOfCoupons() - 1;
        if (numberOfCoupons < 1) {
            couponRepository.delete(coupon);
        } else {
            coupon.setNumberOfCoupons(numberOfCoupons);
            couponRepository.save(coupon);
        }
    }

    @Scheduled(cron = "${schedulingProcessTempTransaction}")
    private void deleteExpiresCoupons() {
        couponRepository.deleteByExpirationDateBefore(LocalDateTime.now());
    }

    /******************************************************************************************** */

}