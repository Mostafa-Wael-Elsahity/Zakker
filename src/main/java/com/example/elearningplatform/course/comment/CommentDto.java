package com.example.elearningplatform.course.comment;

import com.example.elearningplatform.user.UserDto;

import lombok.Data;

@Data
public class CommentDto {

    private Integer id;
    private Boolean isVotedByUser = false;
    private Boolean isCreatedByUser = false;
    private UserDto user;
    private String content;
    private Integer numberOfVotes = 0;
    private Integer numberOfReplyes = 0;
    // private List<CommentDto> replys = new ArrayList<>();


}
