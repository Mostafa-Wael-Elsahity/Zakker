package com.example.elearningplatform.course.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findByCourseId(Integer courseId);

    public List<Review> findByUserId(Integer userId);
}
