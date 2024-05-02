package com.example.elearningplatform.course.lesson.dto;

import com.example.elearningplatform.course.lesson.Lesson;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class LessonDto {

    /********************************************************************* */

    private Integer id;
    private String title;
    private String description;
    private String type;
    private String videoUrl;

    public LessonDto(Lesson lesson) {

        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
        this.type = lesson.getType();
        if(lesson.getFree())
            this.videoUrl = lesson.getVideoUrl();
            else
                this.videoUrl = null;
  


    }

}

