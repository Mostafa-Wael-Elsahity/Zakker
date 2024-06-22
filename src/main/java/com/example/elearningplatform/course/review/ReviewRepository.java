package com.example.elearningplatform.course.review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.course.Course;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findByCourseId(Integer courseId);

    public List<Review> findByUserId(Integer userId);

    /**********************************************************************************/
    @Query("""
            SELECT r FROM Review r
            WHERE r.course.id = :courseId And r.user.id = :userId
            """)
    Optional<Review> findReviewsByCourseIdAndUserId(@Param("courseId") Integer courseId,
            @Param("userId") Integer userId);

    /**********************************************************************************/
    @Modifying
    @Query("""
                    DeLETE FROM Review r
                    WHERE r.course.id = :courseId And r.user.id = :userId
                    """)
    void deleteReviewsByCourseIdAndUserId(@Param("courseId") Integer courseId,
                    @Param("userId") Integer userId);
    /**********************************************************************************/
    @Query("""
            SELECT r.course FROM Review r
            WHERE r.id = :reviewId
            """)
    Optional<Course> findCourseByReviewId(@Param("reviewId") Integer reviewId);

    /**********************************************************************************/
}
