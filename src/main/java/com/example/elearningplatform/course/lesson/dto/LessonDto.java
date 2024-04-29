package com.example.elearningplatform.course.lesson.dto;

import java.math.BigDecimal; // Import the BigDecimal class

import com.example.elearningplatform.course.lesson.Lesson;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class LessonDto {

    /********************************************************************* */

    private Integer id;
    private String title;
    private BigDecimal duration;
    // private String videoUrl;
    // private List<CommentDto> Comments = new ArrayList<>();

    public LessonDto(Lesson lesson) {

        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.duration = lesson.getDuration();
        // this.videoUrl = lesson.getVideoUrl();

    }

}

