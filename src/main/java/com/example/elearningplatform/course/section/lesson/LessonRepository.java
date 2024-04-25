package com.example.elearningplatform.course.section.lesson;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findBySectionId(Integer sectionId);
}
