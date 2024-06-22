package com.example.elearningplatform.course.tag;

import lombok.Data;

@Data
public class CourseTagDto {

    private Integer id;
    private String Tag;

    public CourseTagDto(CourseTag courseTag) {
        this.id = courseTag.getId();
        this.Tag = courseTag.getTag();
    }

}
