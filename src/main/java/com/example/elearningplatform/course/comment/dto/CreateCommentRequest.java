package com.example.elearningplatform.course.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateCommentRequest {
    private Integer lessonId;
    private String content;

}
