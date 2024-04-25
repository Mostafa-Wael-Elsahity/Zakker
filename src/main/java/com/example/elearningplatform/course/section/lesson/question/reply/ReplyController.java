package com.example.elearningplatform.course.section.lesson.question.reply;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class ReplyController {

    private final ReplyService replyService;

    /*************************************************************************************** */
    @GetMapping("/replyes/{commentId}")
    public Response getReplyes(@PathVariable("commentId") Integer commentId) {

        return new Response(HttpStatus.OK, "Success", replyService.getRepliesByCommentId(commentId));

    }

    /*************************************************************************************** */
    @PostMapping("/create-reply")
    public Response createReply(@RequestBody @Valid CreateReply reply, BindingResult result) {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return replyService.createReply(reply);

    }

    /*************************************************************************************** */
    @DeleteMapping("/delete-reply/{replyId}")
    public Response deleteReply(@PathVariable("replyId") Integer replyId) {

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
    @GetMapping("/like-reply/{replyId}/{userId}")
    public Response likeReply(@PathVariable("replyId") Integer replyId, @PathVariable("userId") Integer userId) {

        return replyService.likeReply(replyId, userId);
    }
    /*************************************************************************************** */
    @GetMapping("/remove-like-reply/{replyId}/{userId}")
    public Response removeLikeReply(@PathVariable("replyId") Integer replyId, @PathVariable("userId") Integer userId) {

        return replyService.removeLikeReply(replyId, userId);
    }

}
