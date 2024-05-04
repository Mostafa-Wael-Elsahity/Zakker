package com.example.elearningplatform.course.course.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateCourseRequest {
    private Integer courseId;
    @NotEmpty(message = "Title cannot be empty")
    private String title;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    @NotEmpty(message = "What you will learn cannot be empty")
    private String whatYouWillLearn;
    @NotEmpty(message = "Prerequisite cannot be empty")
    private String prerequisite;
    @NotBlank(message = "Language cannot be empty")
    private String language;
    @NotBlank(message = "Level cannot be empty")
    private String level;
    private List<Integer> categories;
    private List<String> tags;

}