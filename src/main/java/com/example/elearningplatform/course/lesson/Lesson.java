package com.example.elearningplatform.course.lesson;

import java.math.BigDecimal;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.section.Section;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Integer numberOfComments = 0;
    private String title;
    private String type;
    private Boolean free;
    private Boolean isPreviewed;
    private BigDecimal duration;
    private String videoUrl;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "section_id")
    private Section section;

    public void incrementNumberOfComments(Comment comment) {
        this.numberOfComments++;
    }

    public void decrementNumberOfComments(Comment comment) {
        this.numberOfComments--;
    }

}
