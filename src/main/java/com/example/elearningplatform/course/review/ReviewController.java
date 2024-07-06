package com.example.elearningplatform.course.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    /**
     * A description of the entire Java function.
     *
     * @param  courseId   description of parameter
     * @return           description of return value
     */
    @GetMapping("/get-reviews")
    public ReviewResponse getReview(@RequestParam("courseId") Integer courseId) {
        return reviewService.getReviewsByCourseId(courseId);

    }

    /************************************************************************************************ */
    
    /**
     * A description of the entire Java function.
     *
     * @param  request	description of parameter
     * @param  result	description of parameter
     * @return         	description of return value
     */
    @PostMapping("/add-review")
    public Response addReview( @RequestBody @Valid CreateReviewRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return reviewService.addReview(request);

    }

    /************************************************************************************************ */
    
    /**
     * A description of the entire Java function.
     *
     * @param  reviewId	description of parameter
     * @return         	description of return value
     */
    @GetMapping("/delete-review")
    public Response deleteReview(@RequestParam("reviewId") Integer reviewId) {

        return reviewService.deleteReview(reviewId);
    }

    /************************************************************************************************ */
    
    /**
     * Updates a review based on the provided request.
     *
     * @param  request   the UpdateReviewRequest object containing the review details
     * @param  result    the BindingResult object for validation
     * @return           the Response object indicating the result of the review update
     */
    @PostMapping("/update-review")
    public Response updateReview( @RequestBody @Valid UpdateReviewRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return reviewService.updateReview(request);
    }
    /************************************************************************************************ */
    

}
