package com.example.elearningplatform.course.lesson;

import java.math.BigDecimal; // Import the BigDecimal class
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.question.QuestionDto;
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
    private List<QuestionDto> questions = new ArrayList<>();

}

