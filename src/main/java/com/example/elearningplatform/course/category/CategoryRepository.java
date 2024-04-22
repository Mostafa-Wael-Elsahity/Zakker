package com.example.elearningplatform.course.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    @Query(value = """
            SELECT * FROM category Join course_category 
            ON category.id = course_category.category_id 
            WHERE course_category.course_id = ?
            """, nativeQuery = true)
    public List<Category> findByCourseId(Integer id);

}
