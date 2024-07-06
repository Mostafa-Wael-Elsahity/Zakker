package com.example.elearningplatform.course.lesson;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.lesson.dto.CreateLessonRequest;
import com.example.elearningplatform.course.lesson.dto.UpdateLessonRequest;
import com.example.elearningplatform.course.lesson.dto.UploadVideoRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    /*************************************************************************************** */

    /**
     * Retrieves a lesson by its ID.
     *
     * @param  lessonId   the ID of the lesson to retrieve
     * @return            the Response object containing the lesson
     */
    @GetMapping("/get-lesson")
    public Response getLesson(@RequestParam("lessonId") Integer lessonId
           ) {
        return lessonService.getLesson(lessonId);
    }

    /*********************************************************************************************/
    
    /**
     * Creates a new lesson based on the provided CreateLessonRequest.
     *
     * @param  createLessonRequest   the CreateLessonRequest object containing the lesson details
     * @param  result                the BindingResult object for validation
     * @return                       the Response object indicating the result of the lesson creation
     */
    @PostMapping("/create-lesson")
    public Response createLesson(@RequestBody @Valid CreateLessonRequest createLessonRequest,
            BindingResult result) throws IOException, InterruptedException {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return lessonService.createLesson(createLessonRequest);
    }

    /***************************************************************************************/

    /**
     * Uploads a video for a lesson using the provided UploadVideoRequest object.
     *
     * @param  uploadVideoRequest  the UploadVideoRequest object containing the video details
     * @return                     the Response object indicating the result of the video upload
     * @throws IOException         if an I/O error occurs during the upload process
     * @throws InterruptedException  if the upload process is interrupted
     */
    @PutMapping(value = "/upload-video", consumes = { "multipart/form-data" })
    public Response uploadVideo(UploadVideoRequest uploadVideoRequest)
            throws IOException, InterruptedException {

        return lessonService.uploadVideo(uploadVideoRequest);
    }

    /***************************************************************************************/
    
    /**
     * Updates a lesson based on the provided UpdateLessonRequest.
     *
     * @param  updateLessonRequest   the UpdateLessonRequest object containing the lesson details
     * @param  result                the BindingResult object for validation
     * @return                       the Response object indicating the result of the lesson update
     */
    @PostMapping("/update-lesson")
    public Response updateLesson(@RequestBody @Valid UpdateLessonRequest updateLessonRequest,BindingResult result)
            throws IOException, InterruptedException {
                if (result.hasErrors()) {
                    return Validator.validate(result);
                }
        return lessonService.updateLesson(updateLessonRequest);
    }

    /***************************************************************************************/
    
    /**
     * Updates a video for a lesson using the provided UploadVideoRequest object.
     *
     * @param  uploadVideoRequest  the UploadVideoRequest object containing the video details
     * @return                     the Response object indicating the result of the video update
     * @throws IOException         if an I/O error occurs during the update process
     * @throws InterruptedException  if the update process is interrupted
     */
    @PostMapping(value = "/update-video", consumes = { "multipart/form-data" })
    public Response updateVideo(UploadVideoRequest uploadVideoRequest)
            throws IOException, InterruptedException {


        return lessonService.updateVideo(uploadVideoRequest);
    }

    /***************************************************************************************/

    /**
     * Deletes a lesson with the given lesson ID.
     *
     * @param  lessonId  the ID of the lesson to be deleted
     * @return           a Response object indicating the result of the deletion
     */
    @DeleteMapping("/delete-lesson")
    public Response deleteLesson(@RequestParam("lessonId") Integer lessonId) {

        return lessonService.deleteLesson(lessonId);
    }
    /***************************************************************************************/
}
