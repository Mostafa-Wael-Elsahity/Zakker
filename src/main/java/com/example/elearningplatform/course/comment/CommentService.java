package com.example.elearningplatform.course.comment;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.comment.dto.CommentDto;
import com.example.elearningplatform.course.comment.dto.CreateCommentRequest;
import com.example.elearningplatform.course.comment.dto.UpdateCommentRequest;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import aj.org.objectweb.asm.Type;
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
    private final CourseService courseService;

    /************************************************************************************** */

    /************************************************************************************** */

    public Response createComment(CreateCommentRequest createComment) {
        try {
            Lesson lesson = lessonRepository.findById(createComment.getLessonId())
                    .orElseThrow(() -> new Exception("Lesson not found"));
            checkCommentAuth(lesson.getId());
            lesson.incrementNumberOfComments();
            lessonRepository.save(lesson);

            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            Comment comment = new Comment();
            comment.setContent(createComment.getContent());
            comment.setUser(user);
            comment.setLesson(lesson);
            commentRepository.save(comment);

            return new Response(HttpStatus.OK, "Comment created successfully",
                    new CommentDto(comment, false, true));

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */
    public Response deleteComment(Integer commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));


            if (!tokenUtil.getUserId().equals(comment.getUser().getId())) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            Lesson lesson = commentRepository.findLesson(commentId)
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
            lesson.decrementNumberOfComments();
            lessonRepository.save(lesson);
            commentRepository.delete(comment);

            return new Response(HttpStatus.OK, "Comment deleted successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */
    public Response updateComment(UpdateCommentRequest request) {
        try {
            Comment comment = commentRepository.findById(request.getCommentId())
                    .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
            if (!comment.getUser().getId().equals(tokenUtil.getUserId())) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            comment.setContent(request.getContent());
            commentRepository.save(comment);
            return new Response(HttpStatus.OK, "Comment updated successfully",
                    new CommentDto(comment, false, true));
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */
    public Response likeComment(Integer commentId) {
        try {

            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
            Lesson lesson = commentRepository.findLesson(commentId)
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
            checkCommentAuth(lesson.getId());
            commentRepository.likeComment(tokenUtil.getUserId(), commentId);

            comment.incrementNumberOfLikes();
            commentRepository.save(comment);
            // User user = userRepository.findById(userId).orElseThrow(() -> new
            // Exception("User not found"));
            // List<User> likes = commentRepository.findLikes(commentId);
            // likes.add(user);
            // comment.setLikes(likes);
            // commentRepository.save(comment);

            return new Response(HttpStatus.OK, "Comment liked successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /**
     * @throws Exception ************************************************************************************
     */

    public List<CommentDto> getCommentsByLessonId(Integer lessonId, Integer pageNumber) throws Exception {

        checkCommentAuth(lessonId);

            List<Comment> commentes = commentRepository.findByLessonId(lessonId, PageRequest.of(pageNumber, 5));
            List<Comment> likedComments = commentRepository.findLikedCommentsByUserIdAndLesson(tokenUtil.getUserId(),
                    lessonId);

            List<CommentDto> commentesDto = commentes.stream().map(comment -> new CommentDto(
                    comment,
                    likedComments.contains(comment),
                    comment.getUser().getId().equals(tokenUtil.getUserId()))).toList();
            return commentesDto;


    }

    public Response removeLikeComment(Integer commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));

            Lesson lesson = commentRepository.findLesson(commentId)
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
            checkCommentAuth(lesson.getId());
            comment.decrementNumberOfLikes();
            commentRepository.save(comment);
            commentRepository.removeLikeFromComment(tokenUtil.getUserId(), commentId);

            return new Response(HttpStatus.OK, "Comment unliked successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
    @Transactional
    public void checkCommentAuth(Integer lessonId) throws Exception {

        Course course = lessonRepository.findCourseByLessonId(lessonId)
                .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        if (courseService.ckeckCourseSubscribe(course.getId()) == false)
            throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
