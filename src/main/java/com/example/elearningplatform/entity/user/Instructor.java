package com.example.elearningplatform.entity.user;

import com.example.elearningplatform.entity.course.Course;
import com.example.elearningplatform.entity.course.Section;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "instructor")
@Data
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // connect to user to allow multiple instructors
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "instructor")
    private List<Section> sections;

    @ManyToMany
    @JoinTable(name = "instructor_course",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;


}
