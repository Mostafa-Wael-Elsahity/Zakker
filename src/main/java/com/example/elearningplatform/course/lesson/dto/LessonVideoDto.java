package com.example.elearningplatform.course.lesson.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.comment.dto.CommentDto;
import com.example.elearningplatform.course.lesson.note.dto.NoteDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LessonVideoDto {

    private String videoUrl;
    private List<CommentDto> Comments = new ArrayList<>();
    private NoteDto notes;

}
