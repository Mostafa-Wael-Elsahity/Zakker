package com.example.elearningplatform.course.section.dto;

import java.math.BigDecimal; // Import the BigDecimal class
import java.util.List; // Import the List class

import com.example.elearningplatform.course.lesson.dto.LessonDto;
import com.example.elearningplatform.course.section.Section;

import lombok.Data;
import java.util.ArrayList; // Import the ArrayList class

@Data
public class SectionDto {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal duration;
    private Integer numberOfLessons = 0;
    List<LessonDto> lessons =new ArrayList<>();
    public SectionDto(Section section) {
        this.id = section.getId();
        this.title = section.getTitle();
        this.description = section.getDescription();
        this.duration = section.getDuration();
        
    }
    
}