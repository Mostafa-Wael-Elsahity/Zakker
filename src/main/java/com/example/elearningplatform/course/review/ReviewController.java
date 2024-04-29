package com.example.elearningplatform.course.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.ReviewResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/review")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/get-reviews/{courseId}")
    public ReviewResponse getReview(@PathVariable("courseId") Integer courseId) {
        return reviewService.getReviewsByCourseId(courseId);

    }
    

}
