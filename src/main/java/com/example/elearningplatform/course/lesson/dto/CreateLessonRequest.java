package com.example.elearningplatform.course.lesson.dto;

import lombok.Data;

@Data
public class CreateLessonRequest {


    private String description;
    private Integer courseId;
    private Integer sectionId;
    private String title;
    // private String type;
    private Boolean free;


}
