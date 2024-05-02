package com.example.elearningplatform.course.section.dto;

import lombok.Data;

@Data
public class CreateSectionRequest {

    private Integer courseId;

    private String title;
    private String description;

}
