package com.example.elearningplatform.course.section.lesson.question;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateQuestion {
    @NotEmpty(message = "Content cannot be empty")
    private String content;
}
