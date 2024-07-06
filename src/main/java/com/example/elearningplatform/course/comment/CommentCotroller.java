package com.example.elearningplatform.course.comment;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    /**
     * Retrieves comments based on the lesson ID and page number.
     *
     * @param  lessonId    the ID of the lesson
     * @param  pageNumber   the page number for pagination
     * @return             the Response object containing the comments
     */
    @GetMapping("/get-comments/{lessonId}/{pageNumber}")
    public Response getComments(@PathVariable("lessonId") Integer lessonId,
            @PathVariable("pageNumber") Integer pageNumber) throws Exception {

        return commentService.getCommentsByLessonId(lessonId, pageNumber);

    }

    /*************************************************************************************** */

    /*************************************************************************************** */

    /**
     * Creates a new comment based on the provided CreateCommentRequest.
     *
     * @param  comment   the CreateCommentRequest object containing the comment details
     * @param  result    the BindingResult object for validation
     * @return           the Response object indicating the result of the comment creation
     */
    @PostMapping("/create-comment")
    public Response createComment(@RequestBody @Valid CreateCommentRequest comment, BindingResult result) {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return commentService.createComment(comment);
    }

    
    /**
     * Updates a comment based on the provided UpdateCommentRequest.
     *
     * @param  request   the UpdateCommentRequest object containing the comment details
     * @param  result    the BindingResult object for validation
     * @return           the Response object indicating the result of the comment update
     * @throws Exception if an error occurs during the update process
     */
    @PostMapping("/update-comment")
    public Response updateComment(@RequestBody @Valid UpdateCommentRequest request, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return commentService.updateComment(request);
    }

    /*************************************************************************************** */

    /**
     * Deletes a comment by its ID.
     *
     * @param  commentId  the ID of the comment to be deleted
     * @return             a Response object indicating the result of the deletion
     */
    @DeleteMapping("/delete-comment/{commentId}")
    public Response deleteComment(@RequestParam("commentId") Integer commentId) {

        return commentService.deleteComment(commentId);
    }

    /*************************************************************************************** */
    
    
    /**
     * A description of the entire Java function.
     *
     * @param  commentId   description of parameter
     * @return             description of return value
     */
    @PostMapping("/like-comment/{commentId}")
    public Response likeComment(@RequestParam("commentId") Integer commentId) {

        return commentService.likeComment(commentId);
    }
    /*************************************************************************************** */

    /**
     * A description of the entire Java function.
     *
     * @param  commentId  the ID of the comment to remove the like from
     * @return             the Response object indicating the result of removing the like from the comment
     */
    @DeleteMapping("/remove-like-comment/{commentId}")
    public Response removeLikeComment(@RequestParam("commentId") Integer commentId) {
        return commentService.removeLikeComment(commentId);
    }
}
