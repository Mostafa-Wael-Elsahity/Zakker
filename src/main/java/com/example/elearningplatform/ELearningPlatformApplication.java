package com.example.elearningplatform;

import java.util.Locale.Category;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.elearningplatform.user.User;

@SpringBootApplication
@SuppressWarnings({ "rawtypes", "unused" })
public class ELearningPlatformApplication extends GenerateData
        implements ApplicationRunner {

    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // generateData();
        // System.out.println(courseService.getCourse(1).getReviews());
        // System.out.println(categoryService.findByName(Category.class, "C"));
        System.out.println("HeLLO IN elearning platform");

    }
}
