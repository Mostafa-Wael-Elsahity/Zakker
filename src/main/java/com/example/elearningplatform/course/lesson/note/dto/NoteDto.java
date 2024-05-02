package com.example.elearningplatform.course.lesson.note.dto;

import com.example.elearningplatform.course.lesson.note.Note;

import lombok.Data;

@Data
public class NoteDto {
    private Integer id;
    private String content;

    public NoteDto(Note note) {
        this.id = note.getId();
        this.content = note.getContent();
    }
    
}
