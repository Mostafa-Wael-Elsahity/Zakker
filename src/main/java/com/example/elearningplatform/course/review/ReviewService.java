 package com.example.elearningplatform.course.review;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Data
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public List<Review> getReviewsByCourseId (Integer courseID){
        if(courseID == null)return null;
        return reviewRepository.findByCourseId(courseID);
    }
    public List<Review> getReviewsByUserId (Integer userId){
        if(userId == null)return null;
        return reviewRepository.findByUserId(userId);
    }
    // public List<Review> getReviewsByVote (Integer voteId){
    //     if(voteId == null)return null;
    //     return reviewRepository.findByVote(voteId);
    // }

    
}