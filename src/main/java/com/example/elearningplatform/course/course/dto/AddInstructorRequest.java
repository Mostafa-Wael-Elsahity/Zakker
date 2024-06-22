package com.example.elearningplatform.course.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInstructorRequest {
    private Integer courseId;
    private String instructorEmail;
}
