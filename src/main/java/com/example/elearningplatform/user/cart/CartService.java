package com.example.elearningplatform.user.cart;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    // private final HttpServletRequest request;
    private final TokenUtil tokenUtil;

    // public CartDto getCart(String token) {
    public CartDto getCart() {
    // System.out.println("token : " + tokenUtil.getUserId());

        CartDto cartDto = cartRepository.findByUserId(
            tokenUtil.getUserId())
            .map(cart -> new CartDto(cart)).orElse(null);

        return cartDto;
    }

}
