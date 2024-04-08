package com.example.elearningplatform.course.lesson;

import java.math.BigDecimal; // Import the BigDecimal class

import lombok.Data;

@Data
public class LessonDto {
    private String title;
    private BigDecimal duration;

}
