package com.example.elearningplatform.course.course;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
@Data
public class CreateCourseRequesrt {
    @NotEmpty(message = "Title cannot be empty")
    private String title;
    private String whatYouWillLearn;
    private String prerequisite;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    @NotEmpty(message = "Language cannot be empty")
    private String language;
    @NotEmpty(message = "Level cannot be empty")
    private String level;
    private Double price;
    private byte[] image;
}
