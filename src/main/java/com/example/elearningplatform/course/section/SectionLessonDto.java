package com.example.elearningplatform.course.section;

import java.math.BigDecimal; // Import the BigDecimal class
import java.util.*;
import lombok.Data;
import com.example.elearningplatform.course.lesson.LessonVideoDto;

@Data
public class SectionLessonDto {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal duration;
    List<LessonVideoDto> lessonDtos = new ArrayList<>();

    public void addLesson(LessonVideoDto lessonDto) {

        lessonDtos.add(lessonDto);
    }

    public SectionLessonDto(Section section) {
        this.id = section.getId();
        this.title = section.getTitle();
        this.description = section.getDescription();
        this.duration = section.getDuration();
    }
}
