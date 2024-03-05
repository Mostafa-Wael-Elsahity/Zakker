package com.example.elearningplatform.course.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.base.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class TagService extends BaseRepository {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***************************************************************************************************** */
    public List<Tag> findByCourseId(Integer id) {
        String sql = "SELECT * FROM tag WHERE id IN (SELECT tag_id FROM course_tag WHERE course_id = " + id + ")";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class));
    }

    /***************************************************************************************************** */

    // public List<Tag> getAllTags() {
    // String sql = "SELECT * FROM tag";
    // return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class));
    // }

    /***************************************************************************************************** */

    // public Tag getTagById(Integer id) {
    // String sql = "SELECT * FROM tag WHERE id = ?";
    // List<Tag> tags = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper<>(Tag.class), id);
    // if (tags.isEmpty()) {
    // return null;
    // }
    // return tags.get(0);
    // }

    /***************************************************************************************************** */

    // public Tag getTagByName(String name) {
    // String sql = "SELECT * FROM tag WHERE name = ?";
    // List<Tag> tags = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper<>(Tag.class), name);
    // if (tags.isEmpty()) {
    // return null;

    // }
    // return tags.get(0);
    // }

    /***************************************************************************************************** */
    // @Transactional

    // public Tag createTag(Tag tage) {
    // String sql = "INSERT INTO tag (name) VALUES ('" + tage.getName() + "')";
    // jdbcTemplate.update(sql);
    // return getTagByName(tage.getName());
    // }

    /***************************************************************************************************** */
    // @Transactional

    // public void deleteTagById(Integer id) {
    // String sql = "DELETE FROM tag WHERE id =? ";
    // jdbcTemplate.update(sql, id);
    // }

    /***************************************************************************************************** */
    // @Transactional

    // public Tag updateTag(Tag tag) {
    // String sql = "UPDATE tag SET name = '" + tag.getName() + "' WHERE id = " +
    // tag.getId();
    // jdbcTemplate.update(sql);
    // return getTagById(tag.getId());
    // }

    /***************************************************************************************** */
    // @Transactional
    // public void saveTag(Tag tag) {
    // entityManager.persist(tag);
    // }
}
