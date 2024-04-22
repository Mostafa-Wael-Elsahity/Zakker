package com.example.elearningplatform.course.comment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.UserDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TokenUtil tokenUtil;

    /************************************************************************************** */


    public CommentDto mapCommentToDto(Comment comment, Boolean isVotedByUser, Boolean isCreatedByUser) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setNumberOfVotes(comment.getNumberOfVotes());
        commentDto.setNumberOfReplyes(comment.getNumberOfSubComments());
        commentDto.setContent(comment.getContent());
        commentDto.setUser(new UserDto(comment.getUser()));
        commentDto.setIsVotedByUser(isVotedByUser);
        commentDto.setIsCreatedByUser(isCreatedByUser);

        return commentDto;
    }

    /************************************************************************************** */

    public List<CommentDto> getRepliesByCommentId(Integer commentId) {
        List<Comment> commentReplyes = commentRepository.findByParentCommentId(commentId);
        List<CommentDto> commentReplyesDto = new ArrayList<>();
        List<Comment> votedComments = commentRepository.findByVote(tokenUtil.getUserId());
        commentReplyes.forEach(reply -> {
            commentReplyesDto.add(mapCommentToDto(reply, votedComments.contains(reply),
                    reply.getUser().getId().equals(tokenUtil.getUserId())));
        });
        return commentReplyesDto;

    }

    /************************************************************************************** */
    public List<CommentDto> getCommentsByLessonId(Integer lessonId) {

    List<Comment> lessonComments = commentRepository.findByLessonId(lessonId);
    List<CommentDto> lessonCommentDtos = new ArrayList<>();
    List<Comment> votedComments = commentRepository.findByVote(tokenUtil.getUserId());
    lessonComments.forEach(comment -> {
        lessonCommentDtos.add(
                mapCommentToDto(
                        comment, votedComments.contains(comment),
                        comment.getUser().getId().equals(tokenUtil.getUserId())));
    });
    return lessonCommentDtos;
}

    /************************************************************************************** */
}



