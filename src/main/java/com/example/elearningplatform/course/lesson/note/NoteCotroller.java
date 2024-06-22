package com.example.elearningplatform.course.lesson.note;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.lesson.note.dto.CreateNoteRequest;
import com.example.elearningplatform.course.lesson.note.dto.UpdateNoteRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/lesson/note")
public class NoteCotroller {
    private final NoteService NoteService;

    @GetMapping("/get-Notes")
    public Response getNotes(@RequestParam("lessonId") Integer lessonId) throws Exception {

        return  NoteService.getLessonNote(lessonId);

    }

    /*************************************************************************************** */

    /*************************************************************************************** */

    @PostMapping("/create-Note")
    public Response createNote(@RequestBody @Valid CreateNoteRequest Note, BindingResult result) {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return NoteService.createNote(Note);
    }

    /**
     * @throws Exception *************************************************************************************
     */
    @PostMapping("/update-Note")
    public Response updateNote(@RequestBody @Valid UpdateNoteRequest request, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return NoteService.updateNote(request);
    }

    /*************************************************************************************** */

    @DeleteMapping("/delete-Note")
    public Response deleteNote(@RequestParam("NoteId") Integer NoteId) {

        return NoteService.deleteNote(NoteId);
    }

    /*************************************************************************************** */

  
}
