package com.example.elearningplatform.course.reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.comment.CommentRepository;
import com.example.elearningplatform.course.comment.CommentService;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.reply.dto.CreateReplyRequest;
import com.example.elearningplatform.course.reply.dto.ReplyDto;
import com.example.elearningplatform.course.reply.dto.UpdateReplyRequest;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CourseService courseService;

    /************************************************************************************** */
    public Response createReply(CreateReplyRequest createReply) {

        try {
            Lesson lesson = commentRepository.findLesson(createReply.getCommentId()).orElseThrow(
                    () -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
            if (commentService.checkCommentAuth(lesson.getId()).equals(false)) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            User user = userRepository.findById(tokenUtil.getUserId()).orElseThrow(
                    () -> new CustomException("User not found", HttpStatus.NOT_FOUND));
            Comment comment = commentRepository.findById(createReply.getCommentId()).orElseThrow(
                    () -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
            comment.incrementNumberOfReplies();
            commentRepository.save(comment);
            Reply reply = new Reply(createReply, comment, user);

            replyRepository.save(reply);
            // return new Response(HttpStatus.OK, "Reply created successfully",
            // new ReplyDto(reply, false, true));
            return new Response(HttpStatus.OK, "Reply created successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */

    public Response getRepliesByCommentId(Integer commentId, Integer pageNumber) {
        try {
            
            Lesson lesson = commentRepository.findLesson(commentId).orElseThrow(
                    () -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
            if (commentService.checkCommentAuth(lesson.getId()).equals(false)) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            List<Reply> replyes = replyRepository.findByCommentId(commentId, PageRequest.of(pageNumber, 8));
            List<Reply> likedReplyes = replyRepository.findLikedRepliesByUserIdAndCommentId(tokenUtil.getUserId(),
                    commentId);

            List<ReplyDto> replyesDto = replyes.stream().map(reply -> new ReplyDto(
                    reply, likedReplyes.contains(reply), reply.getUser().getId().equals(tokenUtil.getUserId())))
                    .toList();

            return new Response(HttpStatus.OK, "Replies fetched successfully", replyesDto);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());

        }
    }

    /*************************************************************************************************** */
    public Response deleteReply(Integer replyId) {
        try {

            Reply reply = replyRepository.findById(replyId).orElseThrow(
                    () -> new CustomException("Reply not found", HttpStatus.NOT_FOUND));
            if (!reply.getUser().getId().equals(tokenUtil.getUserId())) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            Comment comment = replyRepository.findComment(replyId).orElseThrow(
                    () -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
            comment.decrementNumberOfReplies();
            commentRepository.save(comment);
            replyRepository.delete(reply);
            return new Response(HttpStatus.OK, "Reply deleted successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /*************************************************************************************************** */
    public Response updateReply(UpdateReplyRequest request) {
        try {
            Reply reply = replyRepository.findById(request.getReplyId()).orElseThrow(
                    () -> new CustomException("Reply not found", HttpStatus.NOT_FOUND));
            if (!reply.getUser().getId().equals(tokenUtil.getUserId())) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            reply.setContent(request.getContent());
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply updated successfully",
                    new ReplyDto(reply, false, true));
        } 
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } 
        
        catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /*************************************************************************************************** */
    public Response likeReply(Integer replyId) {
        try {
            if (checkReplyAuth(replyId).equals(false)) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            replyRepository.likeReply(tokenUtil.getUserId(), replyId);
            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new CustomException("Reply not found", HttpStatus.NOT_FOUND));
            reply.incrementNumberOfLikes();
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply liked successfully", null);
        } 
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        }
        catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /*************************************************************************************************** */

    public Response removeLikeReply(Integer replyId) {
        try {

            replyRepository.removeLikeFromReply(tokenUtil.getUserId(), replyId);
            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new CustomException("Reply not found", HttpStatus.NOT_FOUND));
            reply.decrementNumberOfLikes();

            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply unliked successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    public Boolean checkReplyAuth(Integer replyId) {
        try {
        Course course = replyRepository.findCourseByReplyId(replyId).orElseThrow();
        return courseService.ckeckCourseSubscribe(course.getId());

    } catch (Exception e) {
        return false;
    }
    }
}

