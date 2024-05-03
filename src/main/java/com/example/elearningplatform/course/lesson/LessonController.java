package com.example.elearningplatform.course.lesson;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.lesson.dto.CreateLessonRequest;
import com.example.elearningplatform.course.lesson.dto.UploadVideoRequest;
import com.example.elearningplatform.response.Response;

import org.springframework.web.bind.annotation.RequestBody;
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

/*********************************************************************************************/
@PostMapping("/create-lesson")
public Response createLesson(@RequestBody CreateLessonRequest createLessonRequest) throws IOException, InterruptedException {

    return lessonService.createLesson(createLessonRequest);
}

    /***************************************************************************************/
    
    @PutMapping(value = "/upload-video", consumes = { "multipart/form-data" })
    public Response uploadVideo(UploadVideoRequest uploadVideoRequest)
    throws IOException, InterruptedException {
    
        return lessonService.uploadLessonVideo(uploadVideoRequest);
    }
    
    /***************************************************************************************/
    @PostMapping(value = "/update-video", consumes = { "multipart/form-data" })
    public Response updateVideo(UploadVideoRequest uploadVideoRequest)
    throws IOException, InterruptedException {
    
        return lessonService.updateVideo(uploadVideoRequest);
    }
    /***************************************************************************************/

    @DeleteMapping("/delete-lesson/{lessonId}")
    public Response deleteLesson(@PathVariable("lessonId") Integer lessonId) {

        return lessonService.deleteLesson(lessonId);
    }
    /***************************************************************************************/
}
