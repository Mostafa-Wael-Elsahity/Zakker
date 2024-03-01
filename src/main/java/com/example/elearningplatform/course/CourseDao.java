package com.example.elearningplatform.course;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CourseDao {

    private String title;
    private String description;
    private String imageUrl;
    private List<String> instructors;

    @SuppressWarnings("unchecked")
    public CourseDao(Map<String, Object> object) {

        this.title = (String) object.get("title");
        this.description = (String) object.get("description");
        this.imageUrl = (String) object.get("image_url");
        this.instructors = Arrays.asList(object.get("instructors").toString().split(","));

    }

}