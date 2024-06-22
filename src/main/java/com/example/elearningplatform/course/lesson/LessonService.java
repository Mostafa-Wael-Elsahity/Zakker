package com.example.elearningplatform.course.lesson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.elearningplatform.course.comment.CommentService;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.lesson.dto.CreateLessonRequest;
import com.example.elearningplatform.course.lesson.dto.LessonDto;
import com.example.elearningplatform.course.lesson.dto.LessonVideoDto;
import com.example.elearningplatform.course.lesson.dto.UpdateLessonRequest;
import com.example.elearningplatform.course.lesson.dto.UploadVideoRequest;
import com.example.elearningplatform.course.lesson.note.NoteRepository;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;

import jakarta.transaction.Transactional;
import lombok.Data;


@Service
@Data
@Transactional
public class LessonService {
        Logger log = LoggerFactory.getLogger(LessonService.class);

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private NoteRepository noteRepository;

    /*****************************************************************************************************/
    public Response getLesson(Integer lessonId) {
            try {
                    Lesson lesson = lessonRepository.findById(lessonId)
                                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
                    Course course = lessonRepository.findCourseByLessonId(lessonId)
                                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
                    if (lesson.getFree().equals(false))
                    {
                       
                            if(courseService.ckeckCourseSubscribe(course.getId()).equals(false))
                                    throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
                    }
               
            LessonVideoDto lessonVideo = new LessonVideoDto();
            lessonVideo.setVideoUrl(lesson.getVideoUrl());
       

            return new Response(HttpStatus.OK, "Success", lessonVideo);
        } 
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        }
        catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /*****************************************************************************************************************/

    public Response createLesson(CreateLessonRequest createLessonRequest)
                    throws IOException, InterruptedException {
            try {
                    Course course = sectionRepository.findCourse(createLessonRequest.getSectionId())
                                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));

                    Section section = sectionRepository.findById(createLessonRequest.getSectionId())
                                    .orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));

                    HttpResponse<String> response = createLessonRequest(course.getGuid(), course.getApiKey(),
                                    section.getGuid(), createLessonRequest.getTitle());
                    JSONObject responseBodyJson = new JSONObject(response.body());
                    String guid = responseBodyJson.getString("guid");
                    if (response.statusCode() < 200 && response.statusCode() >= 300) {
                            throw new CustomException(response.body(),
                                            HttpStatus.BAD_REQUEST);
                    }
                    Lesson lesson = new Lesson(createLessonRequest);
                    lesson.setGuid(guid);
                    lesson.setSection(section);
                    lesson.setVideoUrl(
                                    String.format(
                                                    "https://iframe.mediadelivery.net/embed/%s/%s",
                                                    course.getGuid(),
                                                    guid));
                    lessonRepository.save(lesson);

                    return new Response(HttpStatus.CREATED, "Video created successfully", new LessonDto(lesson));
            } catch (CustomException e) {
                    return new Response(e.getStatus(), e.getMessage(), null);
            } catch (Exception e) {
                    return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
            }
    }

    /**********************************************************************************************************/
    public HttpResponse<String> createLessonRequest(Integer courseGuid, String ApiKey, String sectionGuid, String title)
                    throws IOException, InterruptedException {

            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(
                                            String.format(
                                                            "https://video.bunnycdn.com/library/%s/videos",
                                                            courseGuid)))
                            .header("accept", "application/json")
                            .header("content-type", "application/json")
                            .header("AccessKey", ApiKey)
                            .method("POST",
                                            HttpRequest.BodyPublishers
                                                            .ofString("{\"title\":\""
                                                                            + title
                                                                            + "\",\"collectionId\":\""
                                                                            + sectionGuid + "\"}"))
                            .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                            HttpResponse.BodyHandlers.ofString());


            return response;

    }

    /**********************************************************************************************************/
    public Response updateLesson(UpdateLessonRequest updateLessonRequest) {
            try {
                    Lesson lesson = lessonRepository.findById(updateLessonRequest.getLessonId())
                                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
                    lesson.setDescription(updateLessonRequest.getDescription());
                    lesson.setTitle(updateLessonRequest.getTitle());
                    lesson.setFree(updateLessonRequest.getFree());
                    lessonRepository.save(lesson);
                    return new Response(HttpStatus.OK, "Lesson updated successfully", new LessonDto(lesson));
            } catch (CustomException e) {
                    return new Response(e.getStatus(), e.getMessage(), null);
            } catch (Exception e) {
                    return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
            }
    }

    /**********************************************************************************************************/

    public Response uploadVideo(UploadVideoRequest uploadVideoRequest)
                    throws IOException, InterruptedException {
            try {

                    Course course = lessonRepository.findCourseByLessonId(uploadVideoRequest.getLessonId())
                                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));

                    Lesson lesson = lessonRepository.findById(
                                    uploadVideoRequest.getLessonId())
                                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

                    HttpResponse<String> response = uploadVideoRequest(lesson.getGuid(), course.getGuid(),
                                    course.getApiKey(), uploadVideoRequest.getVideo());

                    if (response.statusCode() < 200 && response.statusCode() >= 300) {
                            throw new CustomException(response.body(), HttpStatus.BAD_REQUEST);
                    }

                    return new Response(HttpStatus.CREATED, "Video Uploaded successfully", response.body());
            } catch (CustomException e) {
                    return new Response(e.getStatus(), e.getMessage(), null);
            } catch (Exception e) {
                    return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
            }

    }

    /**
     * @throws IOException
     * @throws InterruptedException
     ********************************************************************************************************/
    public HttpResponse<String> uploadVideoRequest(String lessonGuid, Integer courseGuid, String ApiKey,
                    MultipartFile video) throws IOException, InterruptedException {

            byte[] videoBytes = video.getBytes();

            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(String.format(
                                            "https://video.bunnycdn.com/library/%s/videos/%s", courseGuid,
                                            lessonGuid)))
                            .header("accept", "application/json")
                            .header("AccessKey", ApiKey)
                            .method("PUT", HttpRequest.BodyPublishers.ofByteArray(videoBytes))
                            .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                            HttpResponse.BodyHandlers.ofString());


            return response;
    }

    /**********************************************************************************************************/
    public Response updateVideo(UploadVideoRequest uploadVideoRequest) {
            try {
                    Course course = lessonRepository.findCourseByLessonId(uploadVideoRequest.getLessonId())
                                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
                    Lesson lesson = lessonRepository.findById(uploadVideoRequest.getLessonId())
                                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
                    Section section = lessonRepository.findSection(uploadVideoRequest.getLessonId())
                                    .orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));

                    HttpResponse<String> response1 = createLessonRequest(course.getGuid(), course.getApiKey(),
                                    section.getGuid(), lesson.getTitle());

                    JSONObject responseBodyJson = new JSONObject(response1.body());
                    String guid = responseBodyJson.getString("guid");

                    if (response1.statusCode() < 200 && response1.statusCode() >= 300) {

                            throw new CustomException("Error creating video", HttpStatus.BAD_REQUEST);
                    }

                    HttpResponse<String> response2 = uploadVideoRequest(guid,
                                    course.getGuid(),
                                    course.getApiKey(), uploadVideoRequest.getVideo());

                    if (response2.statusCode() < 200 && response2.statusCode() >= 300) {

                            throw new CustomException("Error uploading video", HttpStatus.BAD_REQUEST);

                    }

                    HttpResponse<String> response3 = deleteVideo(course.getGuid(), lesson.getGuid(),
                                    course.getApiKey());

                    lesson.setGuid(guid);
                    lesson.setVideoUrl(
                                    String.format(
                                                    "https://iframe.mediadelivery.net/embed/%s/%s",
                                                    course.getGuid(),
                                                    guid));
                    lessonRepository.save(lesson);

                    return new Response(HttpStatus.OK, "Video updated successfully", response3.body());
            }

            catch (CustomException e) {
                    return new Response(e.getStatus(), e.getMessage(), null);
            } catch (Exception e) {
                    return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);

            }

    }

    /******************************************************************************************************************/

    public Response deleteLesson(Integer lessonId) {
            try {
                    Course course = lessonRepository.findCourseByLessonId(lessonId)
                                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
                    Lesson lesson = lessonRepository.findById(lessonId)
                                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

                    HttpResponse<String> response = deleteVideo(course.getGuid(), lesson.getGuid(),
                                    course.getApiKey());

                    lessonRepository.delete(lesson);
                    return new Response(HttpStatus.OK, "Lesson deleted successfully", response.body());

            } catch (CustomException e) {
                    return new Response(e.getStatus(), e.getMessage(), null);
            } catch (Exception e) {
                    return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
            }

    }

    /***************************************************************************************************/

    public HttpResponse<String> deleteVideo(Integer libraryId, String videoId, String apiKey)
                    throws IOException, InterruptedException {

            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(
                                            String.format(
                                                            "https://video.bunnycdn.com/library/%s/videos/%s",
                                                            libraryId,
                                                            videoId)))
                            .header("accept", "application/json")
                            .header("AccessKey", apiKey)
                            .method("DELETE", HttpRequest.BodyPublishers.noBody())
                            .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                            HttpResponse.BodyHandlers.ofString());

         
            return response;

    }

    /**********************************************************************************************************/

}
