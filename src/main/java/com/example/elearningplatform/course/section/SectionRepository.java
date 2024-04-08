package com.example.elearningplatform.course.section;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    public List<Section> findByCourseId(Integer id);
}
