 package com.example.elearningplatform.course.review;

 import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.review.dto.ReviewDto;
import com.example.elearningplatform.response.ReviewResponse;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Data
@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final TokenUtil tokenUtil;

    /************************************************************************************************* */

    public ReviewResponse getReviewsByCourseId(Integer courseId) {
        try {

            List<ReviewDto> reviews = new ArrayList<>();
            Boolean[] isReviewd = { false };


            courseRepository.findCourseReviews(courseId).forEach(review -> {
                ReviewDto reviewDto = new ReviewDto(review);
           
                if (reviewDto.getUser().getId().equals(tokenUtil.getUserId())) {
                    isReviewd[0] = true;
                    reviews.addFirst(reviewDto);
                }
                else reviews.add(reviewDto);
            });

            return new ReviewResponse(HttpStatus.OK, "Reviews found", reviews, isReviewd[0]);
        } catch (Exception e) {
            return new ReviewResponse(HttpStatus.NOT_FOUND, "Reviews not found", null, false);
        }
    }

    /************************************************************************************************* */

    
}