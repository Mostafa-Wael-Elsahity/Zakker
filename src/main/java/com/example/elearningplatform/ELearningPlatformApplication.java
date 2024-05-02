package com.example.elearningplatform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.user.role.RoleRepository;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.Setter;

@SpringBootApplication
@Setter
@EnableJpaRepositories
public class ELearningPlatformApplication
        implements ApplicationRunner {
    @Autowired
    private GenerateData generateData;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private CourseRepository courseRepository;

    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);

    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("running");
        System.out.println("running");
//         System.out.println("running");User user = userRepository.findById(1602).orElse(null);

// Course course = courseRepository.findById(654).orElse(null);

        
        // for (int i = 1; i <= 10; i++) {

        //     User user = new User();
        //     user.setEmail("user" + i + "@example.com");
        //     user.setFirstName("user" + i);
        //     user.setLastName("mohamed" + i);
        //     user.setPassword(passwordEncoder.encode("password@M.reda.49"));
        //     Role role = roleRepository.findByRole("ROLE_USER").orElse(null);
        //     user.setRoles(List.of(role));

        //     user.setEnabled(true);
        //     user.setLastLogin(java.time.LocalDateTime.now());
        //     user.setRegistrationDate(java.time.LocalDateTime.now());
        //     userRepository.save(user);
        // }
        // for (int i = 1; i <= 10; i++) {
        //     Course course = new Course();
        //     course.setTitle("course" + i);
        //     course.setDescription("Description " + i);
        //     course.setDuration(BigDecimal.valueOf(i));
        //     course.setPrice(i * 100.2);
        //     courseRepository.save(course);
        // }

//         List<User> users = userRepository.findAll();
//         List<Course> courses = courseRepository.findAll();
// //    courses.get(0).setInstructors(users.subList(0, 0));
// //    courseRepository.save(courses.get(0));
// courses.get(0).setEnrolledStudents(users.subList(0, 5));
// courseRepository.save(courses.get(0));



    //    courses.get(0).setInstructors(users.subList(0, 3));
    //    courses.get(0).setEnrolledStudents(users.subList(4,6));
    //    courses.get(1).setEnrolledStudents(users.subList(4,6));
    //    courses.get(2).setEnrolledStudents(users.subList(4,6));
    //    courses.get(3).setEnrolledStudents(users.subList(4,6));
    //    courses.get(4).setEnrolledStudents(users.subList(4,6));
    //    courseRepository.saveAll(courses);
    //    users.get(4).setEnrolledCourses(courses.subList(0, 4));
    //    users.get(5).setEnrolledCourses(courses.subList(0, 4));
    //    users.get(6).setEnrolledCourses(courses.subList(0, 4));
    //    users.get(0).setInstructoredCourses(courses.subList(0, 0));
    //    users.get(1).setInstructoredCourses(courses.subList(0, 0));
    //    users.get(2).setInstructoredCourses(courses.subList(0, 0));
    //    users.get(3).setInstructoredCourses(courses.subList(0, 0));
    //    userRepository.saveAll(users);
        // generateData.truncateDtabase();
        // generateData.createData();
        // Course course = lessonRepository.findCourseByLessonId(603).orElse(null);
        // System.out.println(course);
        // System.out.println(user.);
        // generateData.truncateDtabase();
        // generateData.createData();
        // generateData.setRelationships();
    }
}

