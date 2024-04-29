package com.example.elearningplatform.course.reply.dto;

import com.example.elearningplatform.course.reply.Reply;
import com.example.elearningplatform.user.user.dto.InstructorDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReplyDto {
    private Integer numberOfLikes = 0;
    private Integer id;
    private Boolean isLikedByUser = false;
    private Boolean isCreatedByUser = false;
    private InstructorDto user;
    private String content;

    public ReplyDto(Reply reply, Boolean isLikedByUser, Boolean isCreatedByUser) {

        if (reply == null)
            return;
        this.content = reply.getContent();
        this.isCreatedByUser = isCreatedByUser;
        this.isLikedByUser = isLikedByUser;
        this.id = reply.getId();
      
    }

}
