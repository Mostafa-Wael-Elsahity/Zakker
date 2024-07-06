package com.example.elearningplatform.course.reply;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.reply.dto.CreateReplyRequest;
import com.example.elearningplatform.course.reply.dto.UpdateReplyRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    /*************************************************************************************** */
    
    /**
     * Get replies based on the commentId and pageNumber.
     *
     * @param  commentId   The ID of the comment to retrieve replies for
     * @param  pageNumber   The page number for pagination
     * @return             The response containing the replies for the specified comment
     */
    @GetMapping("/get-replyes")
    public Response getReplyes(@RequestParam("commentId") Integer commentId,
            @RequestParam("pageNumber") Integer pageNumber) {

        return replyService.getRepliesByCommentId(commentId, pageNumber);

    }

    /*************************************************************************************** */
    
    /**
     * Creates a new reply based on the provided CreateReplyRequest.
     *
     * @param  reply    the CreateReplyRequest object containing the reply details
     * @param  result   the BindingResult object for validation
     * @return          the Response object indicating the result of the reply creation
     */
    @PostMapping("/create-reply")
    public Response createReply(@RequestBody @Valid CreateReplyRequest reply, BindingResult result) {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return replyService.createReply(reply);

    }

    /*************************************************************************************** */
    
    /**
     * Deletes a reply based on the provided replyId.
     *
     * @param  replyId   The ID of the reply to be deleted
     * @return           The response indicating the result of the reply deletion
     */
    @DeleteMapping("/delete-reply")
    public Response deleteReply(@RequestParam("replyId") Integer replyId) {

        return replyService.deleteReply(replyId);

    }

    /*************************************************************************************** */
    
    /**
     * Updates a reply based on the provided UpdateReplyRequest.
     *
     * @param  request   the UpdateReplyRequest object containing the reply details
     * @param  result    the BindingResult object for validation
     * @return           the Response object indicating the result of the reply update
     */
    @PostMapping("/update-reply")
    public Response updateReply(@RequestBody @Valid UpdateReplyRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        return replyService.updateReply(request);
    }

    /*************************************************************************************** */
    
    /**
     * Get a response for liking a reply based on the provided replyId.
     *
     * @param  replyId     The ID of the reply to like
     * @return             The response indicating the result of the like operation
     */
    @GetMapping("/like-reply")
    public Response likeReply(@RequestParam("replyId") Integer replyId) {

        return replyService.likeReply(replyId);
    }

    /*************************************************************************************** */
    
    /**
     * Get a response for removing a like from a reply based on the provided replyId.
     *
     * @param  replyId     The ID of the reply to remove the like from
     * @return             The response indicating the result of the like removal operation
     */
    @GetMapping("/remove-like-reply")
    public Response removeLikeReply(@RequestParam("replyId") Integer replyId) {

        return replyService.removeLikeReply(replyId);
    }

}
