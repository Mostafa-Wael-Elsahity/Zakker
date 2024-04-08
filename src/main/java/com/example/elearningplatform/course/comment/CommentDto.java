package com.example.elearningplatform.course.comment;

import com.example.elearningplatform.course.dto.InstructorDto;

import lombok.Data;

@Data
public class CommentDto {

    private Integer id;

    private String content;

    private InstructorDto user;

}
