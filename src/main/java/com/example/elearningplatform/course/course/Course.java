package com.example.elearningplatform.course.course;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.tag.Tag;
import com.example.elearningplatform.user.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "course", indexes = {
    @Index(name = "course_title_index", columnList = "title", unique = false),
})
@NoArgsConstructor
public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Integer id;

        private Integer guid;

        private String title;

        private String ApiKey;

        private String whatYouWillLearn;
        private String prerequisite;

        private String description;

        private String language;

        private String level;
        
        private Boolean isPreviewd;

        private Double price;

        private BigDecimal duration;

        private byte[] image;

        private boolean isPublished;

        private LocalDate creationDate;

        private LocalDate lastUpdateDate;

        private Double totalRatings = 0.0;

        private Integer numberOfRatings = 0;

        private Integer numberOfEnrollments = 0;

        public Course(CreateCourseRequesrt createCourseRequesrt) {
          this.title = createCourseRequesrt.getTitle();
          this.whatYouWillLearn = createCourseRequesrt.getWhatYouWillLearn();
          this.prerequisite = createCourseRequesrt.getPrerequisite();
          this.description = createCourseRequesrt.getDescription();
          this.language = createCourseRequesrt.getLanguage();
          this.level = createCourseRequesrt.getLevel();

          this.totalRatings = 0.0;
          this.numberOfRatings = 0;
          this.numberOfEnrollments = 0;
          this.creationDate = LocalDate.now();
          this.lastUpdateDate = LocalDate.now();
        }

        public void addRating(Double rating) {
          this.totalRatings += rating;
          this.numberOfRatings += 1;
        }
        public void removeRating(Double rating) {
          this.totalRatings -= rating;
          this.numberOfRatings -= 1;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @ToString.Exclude
        @JoinColumn(name = "user_id")
        private User owner;

        @OneToMany(fetch = FetchType.LAZY,mappedBy = "course",cascade = CascadeType.REMOVE)
        @ToString.Exclude
        private List<Section> sections = new ArrayList<>();
        
        @OneToMany(fetch = FetchType.LAZY,mappedBy = "course",cascade = CascadeType.REMOVE)
        @ToString.Exclude
        private List<Review> reviews = new ArrayList<>();

        @ManyToMany(fetch = FetchType.LAZY)
        @ToString.Exclude
        @JoinTable(name = "course_tag", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "tag_id", unique = false))
        private List<Tag> tags = new ArrayList<>();

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "user_id", unique = false))
        @ToString.Exclude
        private List<User> instructors = new ArrayList<>();

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_enrolled_courses", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "user_id", unique = false))
        @ToString.Exclude
        private List<User> enrolledStudents = new ArrayList<>();


        @ManyToMany(fetch = FetchType.LAZY)
        @ToString.Exclude
        @JoinTable(joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "category_id", unique = false))
        private List<Category> categories = new ArrayList<>();

      

       
        public void incrementNumberOfEnrollments() {
          this.numberOfEnrollments++;
        }
        public void decrementNumberOfEnrollments() {
          this.numberOfEnrollments--;
        }

       


}
