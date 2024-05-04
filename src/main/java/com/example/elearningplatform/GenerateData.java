package com.example.elearningplatform;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.category.CategoryRepository;
import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.comment.CommentRepository;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.reply.Reply;
import com.example.elearningplatform.course.reply.ReplyRepository;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.tag.CourseTag;
import com.example.elearningplatform.user.lists.UserList;
import com.example.elearningplatform.user.lists.UserListRepository;
import com.example.elearningplatform.user.role.Role;
import com.example.elearningplatform.user.role.RoleRepository;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class GenerateData {
    private final ReviewRepository reviewRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final CommentRepository commentRepository;
    private final LessonRepository lessonRepository;
    private final CategoryRepository categoryRepository;

    private final PasswordEncoder passwordEncoder;
    private final SectionRepository sectionRepository;
    private final UserListRepository userListRepository;
    private final ReplyRepository replyRepository;


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

        for (int i = 1; i <= 5; i++) {
            Review review = new Review();
            review.setContent("Review " + i);
            review.setRating(5.0);
            review.setCreationDate(java.time.LocalDate.now());
            reviewRepository.save(review);
        }
        for (int i = 1; i <= 5; i++) {
            Comment Comment = new Comment();
            Comment.setContent("Comment " + i);
            commentRepository.save(Comment);
        }
        for (int i = 1; i <= 5; i++) {

            Reply reply = new Reply();
            reply.setContent("Reply " + i);
            replyRepository.save(reply);

        }

        for (int i = 1; i <= 5; i++) {
            Lesson lesson = new Lesson();
            lesson.setTitle("Lesson " + i);
            lesson.setDuration(BigDecimal.valueOf(i));

            lessonRepository.save(lesson);
        }
        for (int i = 1; i <= 5; i++) {

            Section section = new Section();
            section.setTitle("Section " + i);
            section.setDescription("Description " + i);
            section.setDuration(BigDecimal.valueOf(i));
            sectionRepository.save(section);
        }
        for (int i = 1; i <= 5; i++) {
            UserList userList = new UserList();
            userList.setName("List " + i);
            userListRepository.save(userList);
        }
        for (int i = 1; i <= 5; i++) {

            User user = new User();
            user.setEmail("user" + i + "@example.com");
            user.setFirstName("user" + i);
            user.setLastName("mohamed" + i);
            user.setPassword(passwordEncoder.encode("password@M.reda.49"));
            Role role = roleRepository.findByRole("ROLE_USER").orElse(null);
            user.setRoles(List.of(role));

            user.setEnabled(true);
            user.setLastLogin(java.time.LocalDateTime.now());
            user.setRegistrationDate(java.time.LocalDateTime.now());
            userRepository.save(user);
        }
   
        for (int i = 1; i <= 5; i++) {
            Course course = new Course();
            course.setTitle("course" + i);
            course.setDescription("Description " + i);
            course.setDuration(BigDecimal.valueOf(i));
            course.setPrice(i * 100.2);
            courseRepository.save(course);
        }

        

    }

    
    public void truncateDtabase() {
        dropTable("tag");
        dropTable("user_roles");
        dropTable("category");
        dropTable("review");
        dropTable("reply");
        dropTable("comment");
        dropTable("lesson");
        dropTable("section");
        dropTable("cart");
        dropTable("course");
        dropTable("user_list");
        dropTable("users");
        dropTable("address");
        dropTable("comment_likes");
        dropTable("coupon");
        dropTable("instructed_courses");
        dropTable("lists_courses");
        dropTable("reply_likes");
        dropTable("transaction");
        dropTable("users_enrolled_courses");
        dropTable("course_categories");
        dropTable("course_tag");
        dropTable("user_role");
        dropTable("courses_in_cart");

    }

    public void dropTable(String tableName) {
        entityManager.createNativeQuery("DROP TABLE IF EXISTS " + tableName + " CASCADE").executeUpdate();
    }

}





