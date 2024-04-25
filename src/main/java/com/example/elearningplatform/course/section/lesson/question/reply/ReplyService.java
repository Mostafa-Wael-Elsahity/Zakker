package com.example.elearningplatform.course.section.lesson.question.reply;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.section.lesson.question.comment.Comment;
import com.example.elearningplatform.course.section.lesson.question.comment.CommentRepository;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ReplyService {
    private final ReplyRepository replyRepository;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /************************************************************************************** */
    @Transactional
    public Response createReply(CreateReply createReply) {

        try {
            User user = userRepository.findById(tokenUtil.getUserId()).orElseThrow();
            Comment comment = commentRepository.findById(createReply.getCommentId()).orElseThrow();
            comment.incrementNumberOfReplies();
            commentRepository.save(comment);
            Reply reply = new Reply(createReply, user, comment);
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply created successfully", new ReplyDto(reply, false, true));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */

    public List<ReplyDto> getRepliesByCommentId(Integer commentId) {
        List<Reply> replyes = replyRepository.findByCommentId(commentId);

        List<Reply> votedComments = replyRepository.findByLikes(tokenUtil.getUserId());

        List<ReplyDto> replyesDto = replyes.stream().map(reply -> {
            Boolean isVotedByUser = votedComments.contains(reply);
            Boolean isCreatedByUser = reply.getUser().getId().equals(tokenUtil.getUserId());
            return new ReplyDto(reply, isVotedByUser, isCreatedByUser);
        }).toList();
        return replyesDto;

    }

    /*************************************************************************************************** */
    public Response deleteReply(Integer replyId) {
        try {
            Reply reply = replyRepository.findById(replyId).orElseThrow();
            Comment comment = commentRepository.findById(reply.getComment().getId()).orElseThrow();
            if (!reply.getUser().getId().equals(tokenUtil.getUserId())) {
                return new Response(HttpStatus.UNAUTHORIZED, "Unauthorized",
                        "You are not allowed to delete this reply");
            }

            comment.decrementNumberOfReplies();
            commentRepository.save(comment);
            replyRepository.delete(reply);
            return new Response(HttpStatus.OK, "Reply deleted successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /*************************************************************************************************** */
    public Response updateReply(UpdateReplyRequest request) {
        try {
            Reply reply = replyRepository.findById(request.getReplyId()).orElseThrow();
            if (!reply.getUser().getId().equals(tokenUtil.getUserId())) {
                return new Response(HttpStatus.UNAUTHORIZED, "Unauthorized",
                        "You are not allowed to update this reply");
            }
            reply.setContent(request.getContent());
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply updated successfully", new ReplyDto(reply, false, false));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /*************************************************************************************************** */
    public Response likeReply(Integer replyId, Integer userId) {
        try {
            Reply reply = replyRepository.findById(replyId).orElseThrow();
            User user = userRepository.findById(userId).orElseThrow();

            reply.addLike(user);
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply liked successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /*************************************************************************************************** */

    public Response removeLikeReply(Integer replyId, Integer userId) {
        try {
            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new IllegalArgumentException("Reply not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            reply.removeLike(user);
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply unliked successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

}
