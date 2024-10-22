package com.example.elearningplatform.course.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
@Transactional
public class CommentService {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CourseService courseService;

    /************************************************************************************** */

    /************************************************************************************** */

    public Response createComment(CreateCommentRequest createComment) {
        try {
            Lesson lesson = lessonRepository.findById(createComment.getLessonId())
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
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


            if (!comment.getUser().getId().equals(tokenUtil.getUserId())) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

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
            if (checkCommentAuth(lesson.getId()).equals(false)) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            commentRepository.likeComment(tokenUtil.getUserId(), commentId);

            comment.incrementNumberOfLikes();
            commentRepository.save(comment);

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

    public Response getCommentsByLessonId(Integer lessonId, Integer pageNumber) throws Exception {
        try {
            if (checkCommentAuth(lessonId).equals(false)) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            List<Comment> commentes = commentRepository.findByLessonId(lessonId, PageRequest.of(pageNumber, 5));
            List<Comment> likedComments = commentRepository.findLikedCommentsByUserIdAndLesson(tokenUtil.getUserId(),
                    lessonId);

            List<CommentDto> commentesDto = commentes.stream().map(comment -> new CommentDto(
                    comment,
                    likedComments.contains(comment),
                    comment.getUser().getId().equals(tokenUtil.getUserId()))).toList();
            return new Response(HttpStatus.OK, "Comments fetched successfully", commentesDto);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());

    }
}

    public Response removeLikeComment(Integer commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));

            Lesson lesson = commentRepository.findLesson(commentId)
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

            if (checkCommentAuth(lesson.getId()).equals(false)) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
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
    
    public Boolean checkCommentAuth(Integer lessonId) {
        try {

        Course course = lessonRepository.findCourseByLessonId(lessonId)
                .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        return courseService.ckeckCourseSubscribe(course.getId());

    } catch (Exception e) {
        return false;

    }
    }
}
