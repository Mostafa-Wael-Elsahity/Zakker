package com.example.elearningplatform.course.section;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.base.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SectionService extends BaseRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    /***************************************************************************************** */
    // public List<Section> getAllSections() {
    // String sql = "SELECT * FROM section";
    // return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Section.class));
    // }

    /***************************************************************************************** */
    // public Section getSectionById(Integer id) {
    // String sql = "SELECT * FROM section WHERE id = " + id;
    // List<Section> sections = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper(Section.class));
    // if (sections.isEmpty()) {
    // return null;
    // }
    // return sections.get(0);
    // }

    /***************************************************************************************** */

    public List<Section> findByCourseId(Integer id) {
        String sql = "SELECT * FROM section WHERE course_id = " + id;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Section.class));
    }

    /***************************************************************************************** */
    // @Transactional

    // public void deleteById(Integer id) {
    // String sql = "DELETE FROM section WHERE id = " + id;
    // jdbcTemplate.update(sql);
    // }

    /***************************************************************************************** */

    // @Transactional
    // public void saveSection(Section section) {
    // entityManager.persist(section);
    // }

}
