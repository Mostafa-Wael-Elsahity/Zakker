package com.example.elearningplatform.course.lesson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.comment.CommentService;
import com.example.elearningplatform.course.comment.dto.CommentDto;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.lesson.dto.LessonVideoDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;

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

    /*****************************************************************************************************/
    public Response getLesson(Integer lessonId, Integer pageNumber) {
        try {
            commentService.checkCommentAuth(lessonId);
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
            List<CommentDto> comments = commentService.getCommentsByLessonId(lessonId, pageNumber);
            LessonVideoDto lessonVideo = new LessonVideoDto();
            lessonVideo.setContent(lesson.getContent());
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


    /*****************************************************************************************************/

}
