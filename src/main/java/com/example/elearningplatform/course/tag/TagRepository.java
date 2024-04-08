package com.example.elearningplatform.course.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "SELECT * FROM tag WHERE id IN (SELECT tag_id FROM course_tag WHERE course_id = ?)", nativeQuery = true)
    public List<Tag> findByCourseId(Integer id);

}
