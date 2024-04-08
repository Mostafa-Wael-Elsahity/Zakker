package com.example.elearningplatform.course.cart;

import java.sql.SQLException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.example.elearningplatform.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/check-cart")
    public Response checkCart(@RequestHeader Map<String, String> headers) throws SQLException {
        return new Response(HttpStatus.OK, "Success", cartService.checkCart(headers.get("authorization")));
    }

}
