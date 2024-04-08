package com.example.elearningplatform.course.section;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.lesson.Lesson;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "section")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String title;

    private String description;

    private BigDecimal duration;

    @OneToMany(mappedBy = "section")
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

}
