package com.example.elearningplatform.course.lesson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    /*************************************************************************************** */

    @GetMapping("/get-lesson/{lessonId}/{pageNumber}")
    public Response getLesson(@PathVariable("lessonId") Integer lessonId,
            @PathVariable("pageNumber") Integer pageNumber) {
        return lessonService.getLesson(lessonId, pageNumber);
    }

   
    /*************************************************************************************** */


}
