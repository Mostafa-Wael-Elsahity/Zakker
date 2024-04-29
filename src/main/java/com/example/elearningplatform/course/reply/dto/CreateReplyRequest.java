package com.example.elearningplatform.course.reply.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateReplyRequest {
    private Integer commentId;
     @NotEmpty(message = "Content cannot be empty")
    private String content;
}
