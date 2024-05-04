package com.example.elearningplatform.course.tag;

import com.example.elearningplatform.course.course.Course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(name = "course_tag", uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "tag" }))
public class CourseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private String Tag;

}
