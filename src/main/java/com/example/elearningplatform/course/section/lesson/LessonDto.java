package com.example.elearningplatform.course.section.lesson;

import java.math.BigDecimal; // Import the BigDecimal class

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class LessonDto {

    /********************************************************************* */

    private Integer id;
    private String title;
    private BigDecimal duration;
    private String videoUrl;
    // private List<CommentDto> Comments = new ArrayList<>();

}

