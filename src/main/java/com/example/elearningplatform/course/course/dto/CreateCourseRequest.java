package com.example.elearningplatform.course.course.dto;

import lombok.Data;

@Data
public class CreateCourseRequest {
    String title;
    String description;
    String whatYouWillLearn;
    String prerequisite;
    String language;
    String level;
    Integer categoryId;
    Integer tagId;

}
