package com.example.elearningplatform.response;

import com.example.elearningplatform.course.CourseDto;

import lombok.Data;


@Data
public class CourseResponse  {
    private Boolean isSubscribed=false;
    Boolean isreviewed = false;
    private CourseDto course;
    public CourseResponse(Boolean isSubscribed, CourseDto course) {
        this.isSubscribed = isSubscribed;
        this.course = course;
    }
    
}