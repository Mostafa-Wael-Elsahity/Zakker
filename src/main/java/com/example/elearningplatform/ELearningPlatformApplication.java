package com.example.elearningplatform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.lesson.LessonRepository;

import jakarta.transaction.Transactional;
import lombok.Setter;

@SpringBootApplication
@Setter
public class ELearningPlatformApplication
        implements ApplicationRunner {
    @Autowired
    private LessonRepository lessonRepository;


    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);


    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Hello, World!");
        // Course course = lessonRepository.findCourseByLessonId(603).orElse(null);
        // System.out.println(course);
        // System.out.println(user.);

        // generateData.truncateDtabase();
        // generateData.createData();
        // generateData.setRelationships();
    }
}

