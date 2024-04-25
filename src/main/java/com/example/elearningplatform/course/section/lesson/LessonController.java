package com.example.elearningplatform.course.section.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    /*************************************************************************************** */

    @GetMapping("/lessons/{sectionId}")
    public Response getLessons(@PathVariable("sectionId") Integer sectionId) {

        return new Response(HttpStatus.OK, "Success", lessonService.getLessonsBySectionId(sectionId));

    }

    /*************************************************************************************** */
    @GetMapping("/lesson/{lessonId}")
    public Response getLesson(@PathVariable("lessonId") Integer lessonId) {

        return new Response(HttpStatus.OK, "Success", lessonService.getLessonById(lessonId));
    }

    /*************************************************************************************** */


}
