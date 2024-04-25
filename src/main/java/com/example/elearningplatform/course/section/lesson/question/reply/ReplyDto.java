package com.example.elearningplatform.course.section.lesson.question.reply;

import com.example.elearningplatform.course.section.lesson.question.QuestionDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ReplyDto extends QuestionDto {
    private Integer numberOfLikes=-0;
    public ReplyDto(Reply reply, Boolean isVotedByUser, Boolean isCreatedByUser) {
        super(reply, isVotedByUser, isCreatedByUser);
        if (reply == null)
            return;
        this.numberOfLikes = reply.getNumberOfLikes();
    }

}
