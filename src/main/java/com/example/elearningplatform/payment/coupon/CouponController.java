package com.example.elearningplatform.payment.coupon;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.coupon.dto.CreateRequest;
import com.example.elearningplatform.response.Response;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CouponController {

    private final CouponService couponService;

    /****************************** *************************************************** */
    @GetMapping("/create-coupon")
    public Response createCoupon(CreateRequest request) {
        return couponService.createCoupon(request);
    }

    /********************************************************************************* */
    @DeleteMapping("/delete-coupon/{couponId}")
    public Response deleteCoupon(@PathVariable Integer couponId) {
        return couponService.deleteCoupon(couponId);
    }

    @GetMapping("/apply-coupon")
    public Response applyCoupon(@RequestBody ApplyCouponRequest coupon) {
        return couponService.applyCoupon(coupon);
    }

 
    
    
}