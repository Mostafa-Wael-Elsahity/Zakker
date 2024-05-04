package com.example.elearningplatform.course.section.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateSectionRequest {
    private Integer sectionId;

      @NotEmpty(message = "Title cannot be empty")
    private String title;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
}
