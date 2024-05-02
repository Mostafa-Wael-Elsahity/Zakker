 package com.example.elearningplatform.course.review;

 import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.review.dto.CreateReviewRequest;
import com.example.elearningplatform.course.review.dto.ReviewDto;
import com.example.elearningplatform.course.review.dto.UpdateReviewRequest;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
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
    private final CourseService courseService;

    /************************************************************************************************* */

    public Response updateReview(UpdateReviewRequest request) {
        try {
            Review review = reviewRepository.findById(request.getReviewId())
                    .orElseThrow(() -> new CustomException("Review not found", HttpStatus.NOT_FOUND));

            if (!review.getUser().getId().equals(tokenUtil.getUserId()))
                throw new CustomException("You are not authorized to update this review", HttpStatus.UNAUTHORIZED);

            review.setContent(request.getContent());
            review.setRating(request.getRating());
            review.setCreationDate(java.time.LocalDate.now());
            reviewRepository.save(review);

            return new Response(HttpStatus.OK, "Review updated successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "Review not updated", null);
        }
    }

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
        } 
        catch (Exception e) {
            return new ReviewResponse(HttpStatus.NOT_FOUND, "Reviews not found", null, false);
        }
    }

    /************************************************************************************************* */
    public Response deleteReview(Integer reviewId) {
        try {
            // Course course = reviewRepository.findCourseByReviewId(reviewId)
            //         .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));

            // if (courseService.ckeckCourseSubscribe(course.getId()).equals(false))
            //     throw new CustomException("You must subscribe to the course first", HttpStatus.BAD_REQUEST);
            Review review = reviewRepository.findById(reviewId).
            orElseThrow(() -> new CustomException("Review not found", HttpStatus.NOT_FOUND));
            
            if (!review.getUser().getId().equals(tokenUtil.getUserId()))
                throw new CustomException("You are not authorized to delete this review", HttpStatus.UNAUTHORIZED);

            reviewRepository.deleteById(reviewId);
            return new Response(HttpStatus.OK, "Review deleted successfully", null);
        } 
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        }
        catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "Review not deleted", null);
        }
    }

    /************************************************************************************************* */
    public Response addReview(CreateReviewRequest request) {
        try {

            if (courseService.ckeckCourseSubscribe(request.getCourseId()).equals(false))
                throw new CustomException("You must subscribe to the course first", HttpStatus.BAD_REQUEST);
            if (reviewRepository.findReviewsByCourseIdAndUserId(request.getCourseId(), tokenUtil.getUserId())
                    .isPresent())
                throw new CustomException("Review already Exist", HttpStatus.BAD_REQUEST);

            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            course.addRating(request.getRating());
            courseRepository.save(course);

            Review review = new Review();
            review.setContent(request.getContent());
            review.setRating(request.getRating());
            review.setCreationDate(java.time.LocalDate.now());
            review.setCourse(courseRepository.findById(request.getCourseId()).get());
            review.setUser(tokenUtil.getUser());
            reviewRepository.save(review);
            return new Response(HttpStatus.OK, "Review added successfully", new ReviewDto(review));
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "Review not added", null);
        }

    }

    /************************************************************************************************* */

    
}