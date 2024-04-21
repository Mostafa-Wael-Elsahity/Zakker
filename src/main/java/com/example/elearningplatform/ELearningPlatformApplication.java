package com.example.elearningplatform;

import java.math.BigDecimal;

import java.util.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.course.CourseService;
import com.example.elearningplatform.course.cart.CartRepository;
import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.category.CategoryRepository;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.question.Question;
import com.example.elearningplatform.course.question.QuestionRepository;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.tag.Tag;
import com.example.elearningplatform.course.tag.TagRepository;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.Role;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@SuppressWarnings({ "unused" })
public class ELearningPlatformApplication
        implements ApplicationRunner {
    private final ReviewRepository reviewRepository;
    private final EntityManager entityManager;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final TokenUtil tokenUtil;
    private final QuestionRepository questionRepository;
    private final LessonRepository lessonRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private  final PasswordEncoder passwordEncoder;
    private final SectionRepository sectionRepository;

    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Hello, World!");
   

}

    public void createData() {

        for (int i = 1; i < 10; i++) {
            Lesson lesson = new Lesson();
            lesson.setId(i);
            lesson.setTitle("lesson" + i);
            lesson.setDuration(BigDecimal.valueOf(i));
            lesson.setFree(true);
            lesson.setIsPreviewed(true);
            lesson.setVideoUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ " + i);
            lessonRepository.save(lesson);
        }

        for (int i = 0; i < 10; i++) {
            Course course = new Course();
            course.setId(i);
            course.setTitle("course" + i);
            course.setDescription("Description " + i);
            course.setDuration(BigDecimal.valueOf(i));
            course.setPrice(i * 1000.2);
            course.setLanguage("English");
            course.setLevel("Beginner");
            course.setCreationDate(java.time.LocalDate.now());
            course.setLastUpdateDate(java.time.LocalDate.now());
            course.setPublished(true);
            course.setAverageRating(2.5);
            courseRepository.save(course);

        }
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setEmail("user" + i + "@example.com");
            user.setFirstName("user" + i);
            user.setLastName("mohaed" + i);
            user.setPassword("password" + i);
            user.setRoles(List.of(Role.ROLE_USER));
            user.setEnabled(true);
            user.setLastLogin(java.time.LocalDateTime.now());
            user.setRegistrationDate(java.time.LocalDateTime.now());
            userRepository.save(user);
        }

        for (int i = 1; i < 10; i++) {
            Review review = new Review();
            review.setUser(userRepository.findById(i).get());
            review.setCourse(courseRepository.findById(i).get());
            review.setId(i);
            review.setContent("Content " + i);
            review.setRating(5.0);
            review.setCreationDate(java.time.LocalDate.now());
            review.setModificationDate(java.time.LocalDate.now());
            reviewRepository.save(review);
        }

        for (int i = 1; i < 10; i++) {
            Question question = new Question();
            question.setUser(userRepository.findById(i).get());
            question.setLesson(lessonRepository.findById(i).get());

            question.setId(i);
            question.setContent("Content " + i);
            question.setCreationDate(java.time.LocalDate.now());
            question.setModificationDate(java.time.LocalDate.now());
            questionRepository.save(question);
        }

    }

}
