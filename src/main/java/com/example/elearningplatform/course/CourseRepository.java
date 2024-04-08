package com.example.elearningplatform.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c JOIN FETCH  c.instructors i WHERE i.email LIKE :name")
    List<Course> findByInstructorsName(@Param("name") String name);

    @Query("SELECT c FROM Course c WHERE c.title LIKE :title")
    List<Course> findByTitle(@Param("title") String title);

}
