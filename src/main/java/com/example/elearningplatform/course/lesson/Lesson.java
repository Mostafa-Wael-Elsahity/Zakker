package com.example.elearningplatform.course.lesson;

import java.math.BigDecimal;
import java.util.List;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.section.Section;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "lesson", indexes = {
        @Index(name = "section_lesson_id_index", columnList = "section_id", unique = false) })
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private Integer numberOfComments = 0;
    private String title;
    private String type;
    private Boolean free;
    private Boolean isPreviewed;
    private BigDecimal duration;
    private String content;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(fetch = jakarta.persistence.FetchType.LAZY, mappedBy = "lesson", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Comment> comments;

    public void incrementNumberOfComments() {
        this.numberOfComments++;
    }

    public void decrementNumberOfComments() {
        this.numberOfComments--;
    }

}
