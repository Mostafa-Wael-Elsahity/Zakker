package com.example.elearningplatform.course.lesson;

import java.math.BigDecimal;
import java.util.List;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.section.Section;

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
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String title;

    private Boolean isPreviewed;

    private BigDecimal duration;

    private String videoUrl;

    @OneToMany
    @ToString.Exclude
    private List<Comment> comment;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "section_id")
    private Section section;

}
