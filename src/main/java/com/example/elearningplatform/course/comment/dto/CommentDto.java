package com.example.elearningplatform.course.comment.dto;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.user.user.dto.InstructorDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto {
    private Integer id;
    private Boolean isLikedByUser = false;
    private Boolean isCreatedByUser = false;
    private InstructorDto user;
    private String content;

    private Integer numberOfReplyes = 0;
    private Integer numberOfLikes = 0;

    public CommentDto(Comment comment, Boolean isLikedByUser, Boolean isCreatedByUser) {

        if (comment == null)
            return;
        this.content = comment.getContent();
        this.isCreatedByUser = isCreatedByUser;
        this.isLikedByUser = isLikedByUser;
        this.id = comment.getId();
        this.user = new InstructorDto(comment.getUser());
        this.numberOfLikes = comment.getNumberOfLikes();
        this.numberOfReplyes = comment.getNumberOfReplies();
    }

}
