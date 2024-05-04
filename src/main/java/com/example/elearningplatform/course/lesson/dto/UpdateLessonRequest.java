package com.example.elearningplatform.course.lesson.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateLessonRequest {
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    private Integer lessonId;
    @NotEmpty(message = "Title cannot be empty")
    private String title;
    // private String type;
    @NotNull(message = "Free field cannot be null")
    private Boolean free;
}
