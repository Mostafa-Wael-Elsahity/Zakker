package com.example.elearningplatform.course.course.dto;

import lombok.Data;

@Data
public class CreateCourseRequest {
    private String title;
    private String description;
    private String whatYouWillLearn;
    private String prerequisite;
    private String language;
    private String level;
    private Integer categoryId;
    private Integer tagId;

}
