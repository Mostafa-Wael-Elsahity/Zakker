package com.example.elearningplatform.entity.course;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.Duration;
import java.util.List;

@Data
@Entity
@Table(name = "section")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "duration")
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "description")
    private String description;

    // connect with lesson
    @OneToMany(mappedBy = "section")
    private List<Lesson> lessons;

}