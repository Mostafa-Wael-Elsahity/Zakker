package com.example.elearningplatform.payment.coupon;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    
    /**
     * Creates a coupon using the provided request data.
     *
     * @param  request  the request data containing the necessary information to create the coupon
     * @return          the response indicating the result of the coupon creation
     */
    @GetMapping("/create-coupon")
    public Response createCoupon(CreateRequest request) {
        return couponService.createCoupon(request);
    }

    /********************************************************************************* */
    
    /**
     * A description of the entire Java function.
     *
     * @param  couponId	description of parameter
     * @return         	description of return value
     */
    @DeleteMapping("/delete-coupon/{couponId}")
    public Response deleteCoupon(@PathVariable Integer couponId) {
        return couponService.deleteCoupon(couponId);
    }

    /**
     * Applies a coupon to the current request.
     *
     * @param  coupon    the coupon to apply
     * @return            the response indicating the result of the coupon application
     */
    @GetMapping("/apply-coupon")
    public Response applyCoupon(@RequestBody ApplyCouponRequest coupon) {
        return couponService.applyCoupon(coupon);
    }

 
    
    
}