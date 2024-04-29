package com.example.elearningplatform.course.section;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.elearningplatform.course.lesson.Lesson;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    
    public List<Section> findByCourseId(Integer id);

    @Query("""
            SELECT s.lessons FROM Section s WHERE s.id = :sectionId
            """)
    public List<Lesson> findSectionLessons(Integer sectionId);

}
