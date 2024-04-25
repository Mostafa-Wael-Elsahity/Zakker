package com.example.elearningplatform.course.section.lesson.question.reply;

import com.example.elearningplatform.course.section.lesson.question.CreateQuestion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateReply extends CreateQuestion {
    private Integer commentId;
}
