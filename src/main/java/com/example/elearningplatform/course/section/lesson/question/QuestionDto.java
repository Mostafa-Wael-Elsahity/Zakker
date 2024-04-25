package com.example.elearningplatform.course.section.lesson.question;
import com.example.elearningplatform.user.UserDto;

import lombok.Data;

@Data
public class QuestionDto {
    
    private Integer id;
    private Boolean isVotedByUser = false;
    private Boolean isCreatedByUser = false;
    private UserDto user;
    private String content;

    public QuestionDto(Question question, Boolean isVotedByUser, Boolean isCreatedByUser) {

        if (question == null)
            return;
        this.id = question.getId();
        this.content = question.getContent();
        this.user = new UserDto(question.getUser());
        this.isVotedByUser = isVotedByUser;
        this.isCreatedByUser = isCreatedByUser;

    }
}