package com.example.elearningplatform.course.question;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.question.reply.ReplyDto;
import com.example.elearningplatform.user.UserDto;

import lombok.Data;

@Data
public class QuestionDto {

    private Integer id;
    private Boolean isVoted = false;
    private UserDto user;
    private String content;
    private List<ReplyDto> replys = new ArrayList<>();

    public QuestionDto(Question question,Boolean isVoted) {
        if (question == null)
            return;
        this.isVoted = isVoted;
        this.id = question.getId();
        this.content = question.getContent();
        this.user = new UserDto(question.getUser());
        question.getReplys().forEach(reply -> {
            this.replys.add(new ReplyDto(reply));
        });
    }

}
