package com.example.elearningplatform.course.lesson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.base.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class LessonService extends BaseRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /************************************************************************************************ */

    public List<Lesson> findBySectionId(Integer section_id) {
        String sql = "SELECT * FROM lesson WHERE section_id = " + section_id;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Lesson.class));
    }

    /************************************************************************************************ */

    // public Lesson getLessonById(Integer id) {
    // String sql = "SELECT * FROM lesson WHERE id = ?";
    // List<Lesson> lessons = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper<>(Lesson.class), id);
    // if (lessons.isEmpty()) {
    // return null;

    // }
    // return lessons.get(0);
    // }

    /************************************************************************************************ */
    // @Transactional

    // public void deleteLesson(Integer id) {
    // String sql = "DELETE FROM lesson WHERE id = ?";
    // jdbcTemplate.update(sql, id);
    // }

    /************************************************************************************************ */
    // @Transactional
    // public void saveLesson(Lesson lesson) {
    // entityManager.persist(lesson);
    // }
    /************************************************************************************************ */

}
