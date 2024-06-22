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
    @GetMapping("/get-replyes")
    public Response getReplyes(@RequestParam("commentId") Integer commentId,
            @RequestParam("pageNumber") Integer pageNumber) {

        return replyService.getRepliesByCommentId(commentId, pageNumber);

    }

    /*************************************************************************************** */
    @PostMapping("/create-reply")
    public Response createReply(@RequestBody @Valid CreateReplyRequest reply, BindingResult result) {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return replyService.createReply(reply);

    }

    /*************************************************************************************** */
    @DeleteMapping("/delete-reply")
    public Response deleteReply(@RequestParam("replyId") Integer replyId) {

        return replyService.deleteReply(replyId);

    }

    /*************************************************************************************** */
    @PostMapping("/update-reply")
    public Response updateReply(@RequestBody @Valid UpdateReplyRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        return replyService.updateReply(request);
    }
    /*************************************************************************************** */
    @GetMapping("/like-reply")
    public Response likeReply(@RequestParam("replyId") Integer replyId) {

        return replyService.likeReply(replyId);
    }
    /*************************************************************************************** */
    @GetMapping("/remove-like-reply")
    public Response removeLikeReply(@RequestParam("replyId") Integer replyId) {

        return replyService.removeLikeReply(replyId);
    }

}
