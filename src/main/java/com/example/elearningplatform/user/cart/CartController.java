package com.example.elearningplatform.user.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class CartController {
    @Autowired
    private CartService cartService;

    /************************************************************************************************* */
    @GetMapping("/get-cart")
    public Response getCart() {
        return cartService.getCart();
    }

    /***************************************************************************************************/

    @PostMapping("/add-to-cart")
    public Response addToCart(@RequestParam("courseId") Integer courseId) {
        return cartService.addToCart(courseId);
    }

    /****************************************************************************************************/
    @DeleteMapping("/delete-from-cart")
    public Response deleteFromCart(@RequestParam("courseId") Integer courseId)  {
        return cartService.deleteFromCart(courseId);
    }

    /*****************************************************************************************************/

}
