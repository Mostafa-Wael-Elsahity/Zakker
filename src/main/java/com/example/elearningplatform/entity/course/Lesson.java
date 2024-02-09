package com.example.elearningplatform.entity.course;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

@Data
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "video_url")
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @Column(name = "duration")
    private Duration duration;

    @NotBlank
    @Column(name = "description")
    private String description;
}
