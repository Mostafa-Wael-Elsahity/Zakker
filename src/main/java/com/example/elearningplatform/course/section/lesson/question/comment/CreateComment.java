package com.example.elearningplatform.course.section.lesson.question.comment;

import com.example.elearningplatform.course.section.lesson.question.CreateQuestion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateComment extends CreateQuestion {
    private Integer lessonId;

}
