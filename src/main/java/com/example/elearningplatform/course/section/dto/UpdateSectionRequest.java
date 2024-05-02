package com.example.elearningplatform.course.section.dto;

import lombok.Data;

@Data
public class UpdateSectionRequest {
    private Integer sectionId;

    private String title;
    private String description;
}
