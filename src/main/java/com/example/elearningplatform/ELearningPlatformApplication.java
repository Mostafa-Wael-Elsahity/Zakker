package com.example.elearningplatform;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.course.CourseService;
import com.example.elearningplatform.course.cart.Cart;
import com.example.elearningplatform.course.cart.CartRepository;
import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.category.CategoryRepository;
import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.comment.CommentRepository;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.tag.Tag;
import com.example.elearningplatform.course.tag.TagRepository;
import com.example.elearningplatform.response.Response;
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
    private final CommentRepository CommentRepository;
    private final LessonRepository lessonRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;
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
        Category category = new Category();
        category.setName("Programming");
        category.setDescription("Programming");
        categoryRepository.save(category);
        Category category1 = new Category();
        category1.setName("Design");
        category1.setDescription("Design");
        categoryRepository.save(category1);
        Category category2 = new Category();
        category2.setName("Marketing");
        category2.setDescription("Marketing");
        categoryRepository.save(category2);
        Tag tag = new Tag();
        tag.setName("Java");
        tagRepository.save(tag);
        Tag tag1 = new Tag();
        tag1.setName("Python");
        tagRepository.save(tag1);
        Tag tag2 = new Tag();
        tag2.setName("C++");
        tagRepository.save(tag2);



        for (int i = 1; i < 20; i++) {
            User user = new User();
            user.setEmail("user" + i + "@example.com");
            user.setFirstName("user" + i);
            user.setLastName("mohaed" + i);
            user.setPassword("password" + i);
            user.setRoles(List.of(Role.ROLE_USER));
            user.setEnabled(true);
            user.setLastLogin(java.time.LocalDateTime.now());
            user.setRegistrationDate(java.time.LocalDateTime.now());
            userRepository.save(user);
            /******************************************** */
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
            for (int j = i; j < 25; j++) {
                Course course = new Course();
                course.addCategory(category);
                course.addCategory(category1);
                course.addCategory(category2);
course.addTag(tag);
course.addTag(tag1);
course.addTag(tag2);
                course.setTitle("course" + j);
                course.setDescription("Description " + j);
                course.setDuration(BigDecimal.valueOf(j));
                course.setPrice(j * 100.2);
                course.setLanguage("English");
                course.setLevel("Beginner");
                course.setCreationDate(java.time.LocalDate.now());
                course.setLastUpdateDate(java.time.LocalDate.now());
                course.setPublished(true);
                courseRepository.save(course);

                for (int z = j; z < 25; z++) {
                    Review review = new Review();
                    review.setUser(user);
                    review.setCourse(course);
                    review.setContent("Content " + z);
                    review.setRating(5.0);
                    review.setCreationDate(java.time.LocalDate.now());
                    review.setModificationDate(java.time.LocalDate.now());
                    reviewRepository.save(review);
                }

                for (int k = j; k < 25; k++) {
                    Section section = new Section();

                    section.setTitle("section" + k);
                    section.setCourse(course);
                    section.setDescription("Description " + k);
                    section.setDuration(BigDecimal.valueOf(k));
                    sectionRepository.save(section);
                    for (int l = k; l < k + 5; l++) {
                        Lesson lesson = new Lesson();
                        lesson.setTitle("lesson" + l);
                        lesson.setType("Video");
                        lesson.setDuration(BigDecimal.valueOf(l));
                        lesson.setFree(true);
                        lesson.setIsPreviewed(true);
                        lesson.setVideoUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ " + l);
                        lesson.setSection(section);
                        lessonRepository.save(lesson);
                        for (int t = l; t < l + 20; t++) {
                            Comment Comment = new Comment();
                            Comment.setUser(user);
                            Comment.setLesson(lesson);
                            Comment.setContent("Content " + t);
                            Comment.setCreationDate(java.time.LocalDate.now());
                            Comment.setModificationDate(java.time.LocalDate.now());
                            CommentRepository.save(Comment);
                        }

                    }
                }
                courseRepository.save(course);
                cart.addCourse(course);
                user.addCourse(course);
            }
            cartRepository.save(cart);
            userRepository.save(user);

            /******************************************** */

    }
}

}
