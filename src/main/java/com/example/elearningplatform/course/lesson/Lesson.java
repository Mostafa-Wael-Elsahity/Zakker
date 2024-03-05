package com.example.elearningplatform.course.lesson;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.section.Section;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")

    private String title;

    @Column(name = "duration")
    private BigDecimal duration;

    @Column(name = "description")
    private String description;

    @Column(name = "video_url")
    private String videoUrl;

    @OneToMany
    @ToString.Exclude
    private List<Comment> comment;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "section_id")
    private Section section;

}
