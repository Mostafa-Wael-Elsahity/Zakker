package com.example.elearningplatform.course.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.base.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class ReviewService extends BaseRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /************************************************************************************************ */
    public List<Review> findByCourseId(Integer courseId) {
        String sql = "SELECT * FROM review WHERE course_id = " + courseId;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Review.class));
    }

    /************************************************************************************************ */

    public List<Review> findByUserId(Integer userId) {
        String sql = "SELECT * FROM review WHERE user_id = " + userId;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Review.class));
    }

    /************************************************************************************************ */

    // public List<Review> getAllReviews() {
    // String sql = "SELECT * FROM review";
    // return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Review.class));
    // }

    /************************************************************************************************ */

    // public Review getReviewById(Integer id) {
    // String sql = "SELECT * FROM review WHERE id = " + id;
    // List<Review> reviews = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper<>(Review.class));
    // if (reviews.isEmpty()) {
    // return null;

    // }
    // return reviews.get(0);
    // }

    /************************************************************************************************ */
    // @Transactional

    // public void deleteReview(Integer id) {
    // String sql = "DELETE FROM review WHERE id = " + id;
    // jdbcTemplate.update(sql, id);
    // }

    /************************************************************************************************ */
    // @Transactional
    // public void saveReview(Review review) {
    // entityManager.persist(review);
    // }

}
