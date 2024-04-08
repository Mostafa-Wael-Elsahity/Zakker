package com.example.elearningplatform.course.section;

import java.math.BigDecimal; // Import the BigDecimal class
import java.util.List; // Import the List class

import com.example.elearningplatform.course.lesson.LessonDto; // Import the LessonDto class

import lombok.Data;
import java.util.ArrayList; // Import the ArrayList class

@Data

public class SectionDto {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal duration;
    List<LessonDto> lessonDtos;

    public void addLesson(LessonDto lessonDto) {
        if (lessonDtos == null) {
            lessonDtos = new ArrayList<>();
        }
        lessonDtos.add(lessonDto);
    }

    public SectionDto(Section section) {
        this.id = section.getId();
        this.title = section.getTitle();
        this.description = section.getDescription();
        this.duration = section.getDuration();
    }
}