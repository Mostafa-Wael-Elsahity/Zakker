package com.example.elearningplatform.course.lesson;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.elearningplatform.course.comment.CommentService;
import com.example.elearningplatform.course.comment.dto.CommentDto;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.lesson.dto.CreateLessonRequest;
import com.example.elearningplatform.course.lesson.dto.LessonVideoDto;
import com.example.elearningplatform.course.lesson.dto.UploadVideoRequest;
import com.example.elearningplatform.course.lesson.note.Note;
import com.example.elearningplatform.course.lesson.note.NoteRepository;
import com.example.elearningplatform.course.lesson.note.dto.NoteDto;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
@Transactional
public class LessonService {
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
    public Response getLesson(Integer lessonId, Integer pageNumber) {
        try {
            commentService.checkCommentAuth(lessonId);
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
            List<CommentDto> comments = commentService.getCommentsByLessonId(lessonId, pageNumber);
            Note note = noteRepository.findByLessonId(lessonId)
                    .orElse(null);
            LessonVideoDto lessonVideo = new LessonVideoDto();
            lessonVideo.setVideoUrl(lesson.getVideoUrl());
            lessonVideo.setNotes(new NoteDto(note));
            lessonVideo.setComments(comments);

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
            Course course = courseRepository.findById(createLessonRequest.getCourseId())
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));

            Section section = sectionRepository.findById(createLessonRequest.getSectionId())
                    .orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));

            String ApiKey = course.getApiKey();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://video.bunnycdn.com/library/" + course.getGuid() + "/videos"))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .header("AccessKey", ApiKey)
                    .method("POST",
                            HttpRequest.BodyPublishers
                                    .ofString("{\"title\":\"" + createLessonRequest.getTitle()
                                            + "\",\"collectionId\":\""
                                            + section.getGuid() + "\"}"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response.body(),
                    new TypeReference<Map<String, Object>>() {
                    });

            Lesson lesson = new Lesson(createLessonRequest);
            lesson.setGuid(responseMap.get("guid").toString());
            lesson.setSection(section);
            lesson.setVideoUrl("https://iframe.mediadelivery.net/play/" + course.getGuid() + "/"
                    + responseMap.get("guid").toString());
            lessonRepository.save(lesson);

            return new Response(HttpStatus.CREATED, "Video created successfully", response.body());
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /**********************************************************************************************************/

    public Response uploadLessonVideo(UploadVideoRequest uploadVideoRequest)
            throws IOException, InterruptedException {
        try {
            Course course = courseRepository.findById(
                    uploadVideoRequest.getCourseId())
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));

            Lesson lesson = lessonRepository.findById(
                    uploadVideoRequest.getLessonId())
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

            HttpResponse<String> response = uploadVideo(course.getGuid(), lesson.getGuid(),
                    uploadVideoRequest.getVideo(), "PUT", course.getApiKey());
            return new Response(HttpStatus.CREATED, "Video Uploaded successfully", response.body());
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /**********************************************************************************************************/
    public Response updateVideo(UploadVideoRequest uploadVideoRequest) {
        try {
            Course course = courseRepository.findById(uploadVideoRequest.getCourseId())
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            Lesson lesson = lessonRepository.findById(uploadVideoRequest.getLessonId())
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

            HttpResponse<String> response = uploadVideo(course.getGuid(), lesson.getGuid(),
                    uploadVideoRequest.getVideo(), "POST", course.getApiKey());
            if (response.statusCode() == 200) {
                String durationString = getOrDeleteVideo(
                        course.getGuid(), lesson.getGuid(),
                        course.getApiKey(), "GET").get("duration").toString();
                BigDecimal duration = new BigDecimal(durationString);
                lesson.setDuration(duration);
            }
            lessonRepository.save(lesson);

            return new Response(HttpStatus.OK, "Video updated successfully", response.body());
        }

        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());

        }

    }

    /**********************************************************************************************************/

    public HttpResponse<String> uploadVideo(Integer libraryId, String videoId, MultipartFile video, String method,
            String apiKey) throws IOException, InterruptedException {

        byte[] videoBytes = video.getBytes();
        String ApiKey = apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://video.bunnycdn.com/library/" + libraryId + "/videos/"
                        + videoId))
                .header("accept", "application/json")
                .header("AccessKey", ApiKey)
                .method(method, HttpRequest.BodyPublishers.ofByteArray(videoBytes))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    /******************************************************************************************************************/

    public Response deleteLesson(Integer lessonId) {
        try {
            Course course = lessonRepository.findCourseByLessonId(lessonId)
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

            Map<String, Object> responseMap = getOrDeleteVideo(course.getGuid(), lesson.getGuid(), course.getApiKey(),
                    "DELETE");
            if (responseMap.get("success").toString().equals("true")) {
                lessonRepository.delete(lesson);
                return new Response(HttpStatus.OK, "Lesson deleted successfully", null);
            } else {
                throw new CustomException(responseMap.get("message").toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /***************************************************************************************************/

    public Map<String, Object> getOrDeleteVideo(Integer libraryId, String videoId, String apiKey, String method)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://video.bunnycdn.com/" + libraryId + "/videos/" + videoId))
                .header("accept", "application/json")
                .header("AccessKey", apiKey)
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(response.body(),
                new TypeReference<Map<String, Object>>() {
                });
        return responseMap;

    }

    /**********************************************************************************************************/

}
