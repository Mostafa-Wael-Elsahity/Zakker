package com.example.elearningplatform.user.cart;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/get-cart")
    public Response getCart() throws SQLException {
        return new Response(HttpStatus.OK, "Success", cartService.getCart());
    }

    @GetMapping("/add-to-cart")
    public Response addToCart(@RequestParam Integer courseId) throws SQLException {
        return new Response(HttpStatus.OK, "Success", cartService.addToCart(courseId));
    }

    // @GetMapping("/remove-from-cart")
    // public Response removeFromCart(@RequestParam Integer courseId) throws SQLException {
    //     return new Response(HttpStatus.OK, "Success", cartService.removeFromCart(courseId));
    // }

    // @GetMapping("/clear-cart")
    // public Response clearCart() throws SQLException {
    //     return new Response(HttpStatus.OK, "Success", cartService.clearCart());
    // }
}
