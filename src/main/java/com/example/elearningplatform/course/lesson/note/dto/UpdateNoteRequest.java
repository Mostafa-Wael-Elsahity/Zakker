package com.example.elearningplatform.course.lesson.note.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateNoteRequest {
    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private Integer NoteId;
}
