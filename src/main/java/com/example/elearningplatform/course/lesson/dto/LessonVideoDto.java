package com.example.elearningplatform.course.lesson.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.comment.dto.CommentDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LessonVideoDto {

    private String content;
    private List<CommentDto> Comments = new ArrayList<>();
}
