package com.example.elearningplatform.course.section;

import java.math.BigDecimal;

import com.example.elearningplatform.course.Course;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "section",
       indexes = {@Index(name = "course_section_id_index",  columnList="course_id", unique = false)})
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String title;
    private Integer numberOfLessons;

    private String description;

    private BigDecimal duration;
  
    // @OneToMany(mappedBy = "section",fetch = FetchType.LAZY)
    // @ToString.Exclude
    // private List<Lesson> lessons = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    // public void incrementNumberOfLessons() {
    //     this.numberOfLessons++;
    // }

    // public void decrementNumberOfLessons() {
    //     this.numberOfLessons--;
    // }

}
