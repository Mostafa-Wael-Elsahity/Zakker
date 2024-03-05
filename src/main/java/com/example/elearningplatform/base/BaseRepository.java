package com.example.elearningplatform.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.elearningplatform.course.CourseService;
import com.example.elearningplatform.course.category.CategoryService;
import com.example.elearningplatform.course.lesson.LessonService;
import com.example.elearningplatform.course.review.ReviewService;
import com.example.elearningplatform.course.section.SectionService;
import com.example.elearningplatform.course.tag.TagService;
import com.example.elearningplatform.user.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class BaseRepository {

    @Autowired
    @PersistenceContext
    protected EntityManager entityManager;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected SectionService sectionService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected TagService tagService;
    @Autowired
    protected ReviewService reviewService;
    @Autowired
    protected CourseService courseService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected LessonService lessonService;

    /*************************************************************************************************************** */

    public <T> T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    /*************************************************************************************************************** */

    public <T> T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }

    /*************************************************************************************************************** */

    public <T> T delete(T entity) {
        entityManager.remove(entity);
        return entity;
    }

    /*************************************************************************************************************** */

    public <T> T findById(Class<T> entityClass, Object id) {
        return entityManager.find(entityClass, id);
    }

    /*************************************************************************************************************** */

    // public void deleteById(Object Class, Object id) {
    // String sql = "DELETE FROM " + Class + " WHERE id = " + id;
    // jdbcTemplate.update(sql);
    // }
    public <T> List<T> findAll(Class<T> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }
}
