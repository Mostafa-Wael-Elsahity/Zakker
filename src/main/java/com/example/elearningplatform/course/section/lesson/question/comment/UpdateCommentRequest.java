package com.example.elearningplatform.course.section.lesson.question.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateCommentRequest {

    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private Integer commentId;

}
