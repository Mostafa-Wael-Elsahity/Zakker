package com.example.elearningplatform.course;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.tag.Tag;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "course")
public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Integer id;

        private String title;
        private String whatYouWillLearn;
        private String prerequisite;

        private String description;

        private String language;

        private String level;

        private Double price;

        private BigDecimal duration;

        private byte[] image;

        private boolean isPublished;

        private LocalDate creationDate;

        private LocalDate lastUpdateDate;

        private Double totalRatings = 0.0;

        private Integer numberOfRatings = 0;

        private Integer numberOfEnrollments = 0;


        @ManyToMany(fetch = FetchType.EAGER)
        @ToString.Exclude
        @JoinTable(name = "course_tag", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "tag_id", unique = false))
        private List<Tag> tags = new ArrayList<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @ToString.Exclude
        @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "user_id", unique = false))
        private List<User> instructors = new ArrayList<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @ToString.Exclude
        @JoinTable(joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "category_id", unique = false))
        private List<Category> categories = new ArrayList<>();

        public void addCategory(Category category) {
          this.categories.add(category);
        }

        public void addInstructor(User user) {
          this.instructors.add(user);
        }

        public void addTag(Tag tag) {
          this.tags.add(tag);
        }

        public void incrementNumberOfEnrollments() {
          this.numberOfEnrollments++;
        }

        public void incrementNumberOfRatings() {
          this.numberOfRatings++;
        }

        public void addRating(Double rating) {
          this.totalRatings += rating;
        }

}
