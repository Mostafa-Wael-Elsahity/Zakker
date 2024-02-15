package com.example.elearningplatform.entity.course;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.example.elearningplatform.entity.user.Instructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "course")
public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @NotBlank
        @Column(name = "title")
        private String title;

        @NotBlank
        @Column(name = "description")
        private String description;

        @NotBlank
        @Column(name = "language")
        private String language;

        @NotBlank
        @Column(name = "level")
        private String level;

        @NotBlank
        @Column(name = "price")
        private double price;

        @Column(name = "duration")
        private Duration duration;

        @NotBlank
        @Column(name = "imageUrl")
        private String imageUrl;

        // @OneToMany(mappedBy = "course")
        // private List<Section> sections;

        // @ManyToMany
        // @JoinTable(name = "course_category", joinColumns = @JoinColumn(name =
        // "course_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
        // private List<Category> categories;

        @Column(name = "is_published")
        private boolean isPublished;

        @Column(name = "creation_date")
        private LocalDate creationDate;

        @Column(name = "last_update_date")
        private LocalDate lastUpdateDate;

        @Column(name = "average_rating")
        private float averageRating;

        @Column(name = "number_of_ratings")
        private int numberOfRatings;

        @Column(name = "number_of_enrollments")
        private int numberOfEnrollments;

        // @ManyToMany
        // @JoinTable(name = "course_tag", joinColumns = @JoinColumn(name =
        // "course_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
        // private List<Tag> tags;

        // @OneToMany(mappedBy = "course")
        // private List<Review> reviews;

        // @ManyToOne
        // @JoinColumn(name = "instructor_id")
        // private Instructor instructor;
}
