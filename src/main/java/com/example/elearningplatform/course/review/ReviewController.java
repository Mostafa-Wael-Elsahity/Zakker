package com.example.elearningplatform.course.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.review.dto.CreateReviewRequest;
import com.example.elearningplatform.course.review.dto.UpdateReviewRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.response.ReviewResponse;
import com.example.elearningplatform.validator.Validator;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/review")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    /************************************************************************************************ */
    @GetMapping("/get-reviews/{courseId}")
    public ReviewResponse getReview(@PathVariable("courseId") Integer courseId) {
        return reviewService.getReviewsByCourseId(courseId);

    }

    /************************************************************************************************ */
    @PostMapping("/add-review")
    public Response addReview( @RequestBody @Valid CreateReviewRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return reviewService.addReview(request);

    }

    /************************************************************************************************ */
    @GetMapping("/delete-review/{reviewId}")
    public Response deleteReview(@PathVariable("reviewId") Integer reviewId) {

        return reviewService.deleteReview(reviewId);
    }

    /************************************************************************************************ */
    @PostMapping("/update-review")
    public Response updateReview( @RequestBody @Valid UpdateReviewRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return reviewService.updateReview(request);
    }
    /************************************************************************************************ */
    

}
