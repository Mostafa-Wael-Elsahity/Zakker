package com.example.elearningplatform.course.section;

import java.math.BigDecimal;
import java.util.List;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.section.dto.CreateSectionRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name = "section",
       indexes = {@Index(name = "course_section_id_index",  columnList="course_id", unique = false)})
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String guid;

    private String title;
    private Integer numberOfLessons;

    private String description;

    private BigDecimal duration;
  
    @OneToMany(fetch = jakarta.persistence.FetchType.LAZY, mappedBy = "section", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Lesson> lessons;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    public Section(CreateSectionRequest createSectionRequest) {
        this.title = createSectionRequest.getTitle();
        this.description = createSectionRequest.getDescription();
        this.numberOfLessons = 0;

    }


}
