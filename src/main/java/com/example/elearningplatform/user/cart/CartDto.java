package com.example.elearningplatform.user.cart;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.SearchCourseDto;

import lombok.Data;

@Data
public class CartDto {
    private Integer id;

    private Double totalPrice = 0.0;

    private Integer numberOfCourses = 0;
    private List<SearchCourseDto> courses = new ArrayList<>();

   public CartDto(Cart cart) {
        if (cart == null)
            return;
        this.id = cart.getId();
        cart.getCourses().forEach(course -> {
            SearchCourseDto searchCourseDto= new SearchCourseDto(course);
            this.courses.add(searchCourseDto);
        });
        this.totalPrice = cart.getTotalPrice();
        this.numberOfCourses = cart.getNumberOfCourses();
    }
}
