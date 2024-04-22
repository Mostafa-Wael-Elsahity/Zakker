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
    private Integer numberOfSubComments = 0;
    List<LessonDto> lessons =new ArrayList<>();
    
}