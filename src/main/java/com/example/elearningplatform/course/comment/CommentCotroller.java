package com.example.elearningplatform.course.comment;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.comment.dto.CreateCommentRequest;
import com.example.elearningplatform.course.comment.dto.UpdateCommentRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/comment")
public class CommentCotroller {
    private final CommentService commentService;


    @GetMapping("/get-comments/{lessonId}/{pageNumber}")
    public Response getComments(@PathVariable("lessonId") Integer lessonId,
            @PathVariable("pageNumber") Integer pageNumber) throws Exception {

        return new Response(HttpStatus.OK, "Success", commentService.getCommentsByLessonId(lessonId, pageNumber));

    }

    /*************************************************************************************** */

    /*************************************************************************************** */

    @PostMapping("/create-comment")
    public Response createComment(@RequestBody @Valid CreateCommentRequest comment, BindingResult result) {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return commentService.createComment(comment);
    }

    /**
     * @throws Exception ************************************************************************************* */
    @PostMapping("/update-comment")
    public Response updateComment(@RequestBody @Valid UpdateCommentRequest request, BindingResult result) throws Exception {
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
    @GetMapping("/like-comment/{commentId}")
    public Response likeComment(@PathVariable("commentId") Integer commentId) {

        return commentService.likeComment(commentId);
    }
    /*************************************************************************************** */

    @GetMapping("/remove-like-comment/{commentId}")
    public Response removeLikeComment(@PathVariable("commentId") Integer commentId) {

        return commentService.removeLikeComment(commentId);
    }
}
