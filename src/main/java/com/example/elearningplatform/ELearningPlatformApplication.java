package com.example.elearningplatform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.elearningplatform.course.CourseDao;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.course.CourseService;

@SpringBootApplication
@SuppressWarnings({ "rawtypes", "unused" })
public class ELearningPlatformApplication
        implements ApplicationRunner {

    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepository courseRepository;

    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("HeLLO  IN elearning platform");

    }

}