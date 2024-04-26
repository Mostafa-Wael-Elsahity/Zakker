package com.example.elearningplatform.user.cart;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final TokenUtil tokenUtil;
    private final CourseRepository courseRepository;

    
    public CartDto getCart() {

        CartDto cartDto = cartRepository.findByUserId(
            tokenUtil.getUserId())
            .map(cart -> new CartDto(cart)).orElse(null);

        return cartDto;
    }

    public CartDto addToCart(Integer courseId) {
        Cart cart = cartRepository.findByUserId(tokenUtil.getUserId()).orElse(new Cart());
        Course course = new Course();
        course.setId(courseId);
        cart.addCourse(course);
        cartRepository.save(cart);
        return new CartDto(cart);
    }

    // public Object removeFromCart(Integer courseId) {
    //     Course course = courseRepository.findById(courseId)
    //             .orElseThrow(() -> new IllegalArgumentException("Course not found"));

    //     Cart cart = cartRepository.findByUserId(tokenUtil.getUserId())
    //             .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

    //     cart.removeCourse(course);


    // }

    // public Object clearCart() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'clearCart'");
    // }
}
