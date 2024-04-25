package com.example.elearningplatform.course.section.lesson.question.comment;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.section.lesson.Lesson;
import com.example.elearningplatform.course.section.lesson.LessonRepository;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final CommentRepository commentRepository;

    /************************************************************************************** */

    /************************************************************************************** */

    public Response createComment(CreateComment createComment) {
        try {
            Lesson lesson = lessonRepository.findById(createComment.getLessonId()).orElseThrow(() -> new Exception("Lesson not found"));
            lesson.incrementNumberOfComments();
            User user = userRepository.findById(tokenUtil.getUserId()).orElseThrow(() -> new Exception("User not found"));
            lessonRepository.save(lesson);
          
            Comment comment = new Comment(createComment, user, lesson);
            commentRepository.save(comment);
            return new Response(HttpStatus.OK, "Comment created successfully", new CommentDto(comment, false, true));

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */
    public Response deleteComment(Integer commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception("Comment not found"));
            comment.getLesson().decrementNumberOfComments();
            lessonRepository.save(comment.getLesson());
            commentRepository.delete(comment);
            return new Response(HttpStatus.OK, "Comment deleted successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */
    public Response updateComment(UpdateCommentRequest request) {
        try {
            Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(() -> new Exception("Comment not found"));
            comment.setContent(request.getContent());
            commentRepository.save(comment);
            return new Response(HttpStatus.OK, "Comment updated successfully", new CommentDto(comment, false, false));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */
    public Response likeComment(Integer commentId, Integer userId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception("Comment not found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
            comment.addLike(user);
            commentRepository.save(comment);
            return new Response(HttpStatus.OK, "Comment liked successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
    /************************************************************************************** */

    public List<CommentDto> getCommentsByLessonId(Integer commentId) {
        try{
        List<Comment> commentes = commentRepository.findByLessonId(commentId);

        List<Comment> votedComments = commentRepository.findByLikes(tokenUtil.getUserId());

        List<CommentDto> commentesDto = commentes.stream().map(comment -> {
            Boolean isVotedByUser = votedComments.contains(comment);
            Boolean isCreatedByUser = comment.getUser().getId().equals(tokenUtil.getUserId());
            return new CommentDto(comment, isVotedByUser, isCreatedByUser);
        }).toList();
        return commentesDto;
    }
    catch(Exception e){
        return null;
    }

    }

    public Response removeLikeComment(Integer commentId, Integer userId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception("Comment not found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
            comment.removeLike(user);
            commentRepository.save(comment);
            return new Response(HttpStatus.OK, "Comment unliked successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
}
