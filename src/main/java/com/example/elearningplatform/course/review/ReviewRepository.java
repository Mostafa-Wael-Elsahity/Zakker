package com.example.elearningplatform.course.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findByCourseId(Integer courseId);

    public List<Review> findByUserId(Integer userId);

    @Query("""
            SELECT r FROM Review r
            JOIN FETCH  r.votes v WHERE v.id = :id
                """)
    List<Review> findByVote(@Param("id") Integer id);

}
