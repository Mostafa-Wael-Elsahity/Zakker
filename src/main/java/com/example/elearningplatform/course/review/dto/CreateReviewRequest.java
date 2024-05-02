package com.example.elearningplatform.course.review.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewRequest {
    @NotEmpty(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Rating cannot be empty")
    private Double rating;
    private Integer courseId;

}
