package com.example.elearningplatform;

import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.section.lesson.LessonRepository;
import com.example.elearningplatform.course.section.lesson.question.comment.CommentRepository;
import com.example.elearningplatform.response.Response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ReviewRepository reviewRepository;

    private final CourseRepository courseRepository;

    private final CommentRepository commentRepository;
    private final LessonRepository lessonRepository;

    private final SectionRepository sectionRepository;

    @GetMapping(value = "/test")
    public Response getMethodName() {
        RData data = new RData();
        data.start = LocalTime.now().toString();
        for (int i = 0; i < 1000; i++) {
            sectionRepository.findById(i).orElse(null);
            data.num++;
            courseRepository.findById(i).orElse(null);
            data.num++;
            reviewRepository.findById(i).orElse(null);
            data.num++;
            commentRepository.findById(i).orElse(null);
            data.num++;
            lessonRepository.findById(i).orElse(null);
            data.num++;

        }
        data.end = LocalTime.now().toString();

        return new Response(HttpStatus.OK, "Success", data);

    }

}

@Data
class RData {
    int num = 0;
    String start;
    String end;

}
