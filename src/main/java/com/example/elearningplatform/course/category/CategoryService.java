package com.example.elearningplatform.course.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.base.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class CategoryService extends BaseRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /************************************************************************************** */
    // public List<Category> getAllCategories() {
    // String sql = "SELECT * FROM category";
    // return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    // }

    /************************************************************************************** */

    public List<Category> findByCourseId(Integer id) {
        String sql = "SELECT * FROM category WHERE id IN (SELECT category_id FROM course_category WHERE course_id =?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class), id);
    }

    /************************************************************************************** */

    // public Category getCategoryById(Integer id) {
    // String sql = "SELECT * FROM category WHERE id = ?";
    // List<Category> categories = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper<>(Category.class), id);
    // if (categories.isEmpty()) {
    // return null;

    // }
    // return categories.get(0);
    // }

    /************************************************************************************** */

    // public Category getCategoryByName(String name) {
    // String sql = "SELECT * FROM category WHERE name =? ";
    // List<Category> categories = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper<>(Category.class), name);
    // return categories.get(0);
    // }

    /************************************************************************************** */

    // @Transactional
    // public void addCategory(Category category) {
    // String sql = "INSERT INTO category (name, description) VALUES (?, ?)";
    // jdbcTemplate.update(sql, category.getName(), category.getDescription());
    // }

    /************************************************************************************** */

    // @Transactional
    // public void deleteCategory(Integer id) {
    // String sql = "DELETE FROM category WHERE id = ?";
    // jdbcTemplate.update(sql, id);
    // }

    /************************************************************************************** */

    // @Transactional
    // public void updateCategory(Category category) {
    // entityManager.merge(category);
    // }

    /************************************************************************************** */

    @Transactional
    public void deleteByName(String name) {
        String sql = "DELETE FROM category WHERE name = ?";
        jdbcTemplate.update(sql, name);
    }

    /************************************************************************************** */
    // @Transactional
    // public void saveCategory(Category category) {
    // entityManager.persist(category);
    // }

}
