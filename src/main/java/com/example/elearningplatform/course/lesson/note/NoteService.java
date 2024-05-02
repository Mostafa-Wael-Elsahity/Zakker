package com.example.elearningplatform.course.lesson.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.lesson.note.dto.CreateNoteRequest;
import com.example.elearningplatform.course.lesson.note.dto.NoteDto;
import com.example.elearningplatform.course.lesson.note.dto.UpdateNoteRequest;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseService courseService;

    /************************************************************************************************/
    public Response getNotes() {
        try {

            return new Response(HttpStatus.OK, "Note created successfully", null);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************/
    public Response createNote(CreateNoteRequest request) {
        try {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
            checkNoteAuth(lesson.getId());

            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
            Note note = new Note();
            note.setContent(request.getContent());
            note.setUser(user);
            note.setLesson(lesson);
            noteRepository.save(note);

            return new Response(HttpStatus.OK, "Note created successfully", new NoteDto(note));

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************/
    public Response updateNote(UpdateNoteRequest request) {
        try {
            Note note = noteRepository.findById(request.getNoteId())
                    .orElseThrow(() -> new CustomException("Note not found", HttpStatus.NOT_FOUND));
            if (!note.getUser().getId().equals(tokenUtil.getUserId())) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            note.setContent(request.getContent());
            noteRepository.save(note);
            return new Response(HttpStatus.OK, "Note updated successfully",
                    new NoteDto(note));
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************/
    public Response deleteNote(Integer noteId) {
        try {
            Note note = noteRepository.findById(noteId)
                    .orElseThrow(() -> new CustomException("Note not found", HttpStatus.NOT_FOUND));

            if (!note.getUser().getId().equals(tokenUtil.getUserId())) {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            noteRepository.delete(note);

            return new Response(HttpStatus.OK, "Note deleted successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************/

    public Response getNotesByLessonId(Integer lessonId) {
        try {
            checkNoteAuth(lessonId);

            Note note = noteRepository.findByLessonId(lessonId).orElseThrow(
                    () -> new CustomException("Note not found", HttpStatus.NOT_FOUND));

            return new Response(HttpStatus.OK, "Note created successfully", new NoteDto(note));
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    private void checkNoteAuth(Integer lessonId) {
        Course course = lessonRepository.findCourseByLessonId(lessonId)
                .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        if (courseService.ckeckCourseSubscribe(course.getId()).equals(false))
            throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

}
