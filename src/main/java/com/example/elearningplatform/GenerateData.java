package com.example.elearningplatform;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseService;
import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.category.CategoryService;
import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.comment.CommentService;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonService;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewService;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionService;
import com.example.elearningplatform.course.tag.Tag;
import com.example.elearningplatform.course.tag.TagService;
import com.example.elearningplatform.role.Role;
import com.example.elearningplatform.role.RoleService;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserService;

import jakarta.transaction.Transactional;

@Transactional
public class GenerateData {

    @Autowired
    RoleService roleService;
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    TagService tagService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    CommentService commentService;
    @Autowired
    LessonService lessonService;
    @Autowired
    SectionService sectionService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CourseService CourseService;

    public void generateData() {
        Role role = roleService.getByName("ROLE_USER");
        if (role == null) {
            role = new Role("ROLE_USER");
            roleService.saveRole(role);

        }
        // roleService.saveRole(role);

        List<Tag> tags = new ArrayList<Tag>();
        for (int i = 0; i < 5; i++) {
            Tag tag = createTag(i);
            tagService.save(tag);
            tags.add(tag);
        }
        List<Category> categories = new ArrayList<Category>();
        for (int i = 0; i < 5; i++) {
            Category category = createCategory(i);
            categoryService.save(category);
            categories.add(createCategory(i));
        }
        List<Course> courses = new ArrayList<Course>();

        for (int i = 0; i < 2; i++) {
            User user = createUser(List.of(role), i);
            userService.save(user);
            List<Review> reviews = new ArrayList<Review>();
            for (int j = i; j < i + 5; j++) {
                Review review = createReview(user, j);
                reviewService.save(review);
                reviews.add(review);
            }

            for (int j = i; j < i + 5; j++) {
                Course course = createCourse(j, reviews, categories, tags);
                course.setInstructors(List.of(user));
                CourseService.save(course);
                courses.add(course);
            }
            user.setInstructedCourses(courses);
            user.setCourses(courses);
            userService.save(user);
        }
    }

    public Tag createTag(int i) {
        Tag tag = new Tag();
        tag.setName("Tag " + i);
        tagService.save(tag);
        return tag;

    }

    public Review createReview(User user, int i) {
        Review review = new Review();
        review.setUser(user);
        review.setContent("Content " + i);
        review.setRating(5.0);
        review.setCreationDate(java.time.LocalDate.now());
        review.setModificationDate(java.time.LocalDate.now());
        reviewService.save(review);
        return review;
    }

    public Category createCategory(int i) {
        Category category = new Category();
        category.setName("Category " + i);
        category.setDescription("Description " + i);
        categoryService.save(category);
        return category;
    }

    public Comment createComment(User user, Lesson lesson, int i) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent("Content " + i);
        comment.setLesson(lesson);
        commentService.save(comment);
        return comment;
    }

    public Section createSection(Course course, int i) {

        Section section = new Section();
        section.setCourse(course);
        section.setTitle("Section " + i);
        section.setDescription("Description " + i);
        section.setDuration(BigDecimal.valueOf(i));
        sectionService.save(section);

        List<Lesson> lessonList = new ArrayList<Lesson>();
        for (int j = i; j < i + 5; j++) {
            Lesson lesson = createLesson(section, j);
            lessonService.save(lesson);
            lessonList.add(lesson);
        }
        section.setLessons(lessonList);
        sectionService.save(section);
        return section;
    }

    public Lesson createLesson(Section section, int i) {
        Lesson lesson = new Lesson();
        lesson.setSection(section);
        lesson.setTitle("Lesson " + i);
        lesson.setDescription("Description " + i);
        lesson.setVideoUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ " + i);
        lessonService.save(lesson);
        return lesson;
    }

    public User createUser(List<Role> roles, int i) {
        User user = new User();
        user.setEnabled(false);
        user.setEmail("user " + i);
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("User");
        user.setLastName("User");
        user.setPhoneNumber("0123456789");
        user.setRegistrationDate(java.time.LocalDateTime.now());
        user.setProfilePicture(null);
        user.setAge(20);
        user.setBio("bio");
        userService.save(user);
        user.setRoles(roles);
        userService.save(user);
        return user;
    }

    public Course createCourse(int j, List<Review> reviews, List<Category> categories,
            List<Tag> tags) {
        Course course = new Course();
        course.setTitle("Course " + j);
        course.setDescription("Description " + j);
        course.setDuration(BigDecimal.valueOf(j));
        course.setPrice(j * 1000.2);
        course.setLanguage("English");
        course.setLevel("Beginner");
        course.setCreationDate(java.time.LocalDate.now());
        course.setLastUpdateDate(java.time.LocalDate.now());
        course.setPublished(true);
        course.setAverageRating(2.5);
        courseService.save(course);
        List<Section> sections = new ArrayList<Section>();
        for (int i = j; i < j + 5; i++) {
            Section section = createSection(course, i);
            sectionService.save(section);
            sections.add(section);
        }
        course.setSections(sections);
        // instructors);
        course.setReviews(reviews);
        course.setCategories(categories);
        // course.setTags(tags);
        courseService.save(course);
        return course;
    }
}
