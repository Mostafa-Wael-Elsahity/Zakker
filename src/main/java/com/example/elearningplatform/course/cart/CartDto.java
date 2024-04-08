package com.example.elearningplatform.course.cart;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.dto.SerchCourseDto;

import lombok.Data;

@Data
public class CartDto {
    private Integer id;

    private Double totalPrice = 0.0;

    private Integer numberOfCourses = 0;
    private List<SerchCourseDto> courses = new ArrayList<>();

    public void addCourse(SerchCourseDto course) {
        this.courses.add(course);
        this.numberOfCourses++;
        this.totalPrice += course.getPrice();
    }
}
