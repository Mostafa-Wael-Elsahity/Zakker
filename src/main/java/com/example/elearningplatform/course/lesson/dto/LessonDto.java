package com.example.elearningplatform.course.lesson.dto;

import java.math.BigDecimal;

import com.example.elearningplatform.course.lesson.Lesson;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class LessonDto {

    /********************************************************************* */

    private Integer id;
    private String title;
    private String description;
    private String type;
    private BigDecimal duration;
    private Boolean free;

    public LessonDto(Lesson lesson) {
        this.free = lesson.getFree();

        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
        this.type = lesson.getType();
        this.duration = lesson.getDuration();

    }
}

