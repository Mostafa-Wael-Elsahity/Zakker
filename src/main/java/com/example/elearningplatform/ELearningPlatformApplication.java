package com.example.elearningplatform;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.course.CourseService;
import com.example.elearningplatform.course.cart.Cart;
import com.example.elearningplatform.course.cart.CartRepository;
import com.example.elearningplatform.user.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@Transactional
@SuppressWarnings({ "unused" })
public class ELearningPlatformApplication
        implements ApplicationRunner {

    private final EntityManager entityManager;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("HeLLO IN elearning platform");

        // نجيب اخر حاجة اليوز اتفرج عليها
        // الفيديوهات الي اتفرج عليها
    }
}
