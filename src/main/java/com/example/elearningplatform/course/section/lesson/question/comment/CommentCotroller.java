package com.example.elearningplatform.course.section.lesson.question.comment;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentCotroller {
    private final CommentService commentService;

    /*************************************************************************************** */

    @GetMapping("/comments/{lessonId}")
    public Response getComments(@PathVariable("lessonId") Integer lessonId) {

        return new Response(HttpStatus.OK, "Success", commentService.getCommentsByLessonId(lessonId));

    }

    /*************************************************************************************** */

    /*************************************************************************************** */

    @PostMapping("/create-comment")
    public Response createComment(@RequestBody @Valid CreateComment comment, BindingResult result) {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return commentService.createComment(comment);
    }

    /*************************************************************************************** */
    @PostMapping("/update-comment")
    public Response updateComment(@RequestBody @Valid UpdateCommentRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return commentService.updateComment(request);
    }

    /*************************************************************************************** */

    @PostMapping("/delete-comment/{commentId}")
    public Response deleteComment(@PathVariable("commentId") Integer commentId) {

        return commentService.deleteComment(commentId);
    }

    /*************************************************************************************** */
    @GetMapping("/like-comment/{commentId}/{userId}")
    public Response likeComment(@PathVariable("commentId") Integer commentId, @PathVariable("userId") Integer userId) {

        return commentService.likeComment(commentId, userId);
    }
    /*************************************************************************************** */

    @GetMapping("/remove-like-comment/{commentId}/{userId}")
    public Response removeLikeComment(@PathVariable("commentId") Integer commentId, @PathVariable("userId") Integer userId) {

        return commentService.removeLikeComment(commentId, userId);
    }
}
