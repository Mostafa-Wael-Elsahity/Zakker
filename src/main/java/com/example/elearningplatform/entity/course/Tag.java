package com.example.elearningplatform.entity.course;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tag")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String name;

    // @ManyToMany
    // @JoinTable(name = "course_tag",
    // joinColumns = @JoinColumn(name = "tag_id"),
    // inverseJoinColumns = @JoinColumn(name = "course_id"))
    // private List<Course> courses;

}
