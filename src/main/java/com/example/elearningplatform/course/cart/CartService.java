package com.example.elearningplatform.course.cart;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.dto.InstructorDto;
import com.example.elearningplatform.course.dto.SerchCourseDto;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final TokenUtil tokenUtil;

    public CartDto checkCart(String token) throws SQLException {
        String userName = tokenUtil.getUserNameFromToken(token.substring(7, token.length()));
        if (userName == null)
            return null;
        // User user = userRepository.findByEmail(userName).orElse(null);
        // System.out.println("user : " + user);
        // Cart cart = cartRepository.findByUser(user).orElse(null);
        Cart cart = cartRepository.findByUserName(userName).orElse(null);
        CartDto cartDto = new CartDto();
        for (Course c : cart.getCourses()) {
            SerchCourseDto serchCourseDto = new SerchCourseDto(c);
            for (User u : c.getInstructors()) {
                InstructorDto instr = new InstructorDto(u);
                serchCourseDto.addInstructor(instr);
            }
            cartDto.addCourse(serchCourseDto);
        }
        return cartDto;
    }

}
