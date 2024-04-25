package com.example.elearningplatform.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("""
            SELECT c FROM Course c
            JOIN FETCH  c.instructors i WHERE i.id = :id
                """)
    Page<Course> findByInstructorId(@Param("id") Integer id, Pageable pageable);

    @Query("""
            SELECT c FROM Course c WHERE lower(c.title) LIKE lower(concat('%', :title, '%'))
                """)
    Page<Course> findByTitle(@Param("title") String title, Pageable pageable);

@Query("""
            SELECT c FROM Course c
            JOIN FETCH c.categories cat WHERE cat.id = :categoryId
                """)
    Page<Course> findByCategoryId(Integer categoryId, Pageable pageable);

}
