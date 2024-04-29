package com.example.elearningplatform.course.reply.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
@Data
public class UpdateReplyRequest {

    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private Integer replyId;

}
