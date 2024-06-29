package com.example.elearningplatform.course.tag;

import com.example.elearningplatform.course.course.Course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "course_tag", uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "tag" }))
public class CourseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    private Course course;
    private String Tag;

}
