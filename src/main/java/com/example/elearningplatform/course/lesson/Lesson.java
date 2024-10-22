package com.example.elearningplatform.course.lesson;

import java.math.BigDecimal;
import java.util.List;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.lesson.dto.CreateLessonRequest;
import com.example.elearningplatform.course.lesson.note.Note;
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
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name = "lesson", indexes = {
        @Index(name = "section_lesson_id_index", columnList = "section_id", unique = false) })
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String guid;
    private String videoUrl;
    private String title;
    private String description;
    private String type;
    private Boolean free;
    private Boolean isPreviewed;
    private Integer duration;
    private String content;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(fetch = jakarta.persistence.FetchType.LAZY, mappedBy = "lesson", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(fetch = jakarta.persistence.FetchType.LAZY, mappedBy = "lesson", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Note> notes;


    public Lesson(CreateLessonRequest createLessonRequest) {
        this.title = createLessonRequest.getTitle();
        this.description = createLessonRequest.getDescription();
        this.free = createLessonRequest.getFree();

    }

}
