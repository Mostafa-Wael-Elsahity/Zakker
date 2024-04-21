package com.example.elearningplatform.course.question.reply;

import java.time.LocalDate;

import com.example.elearningplatform.user.UserDto;

import lombok.Data;

@Data
public class ReplyDto {

    private Integer id;
    private Boolean isVoted = false;

    private String content;

    private LocalDate creationDate;
    private LocalDate modificationDate;

    private UserDto user;

    public ReplyDto(Reply reply) {
        if (reply == null)
            return;
        this.id = reply.getId();
        this.content = reply.getContent();
        this.creationDate = reply.getCreationDate();
        this.modificationDate = reply.getModificationDate();
        this.user = new UserDto(reply.getUser());

    }
}
