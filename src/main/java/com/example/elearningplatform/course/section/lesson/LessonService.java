package com.example.elearningplatform.course.section.lesson;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.section.lesson.question.comment.CommentRepository;
import com.example.elearningplatform.course.section.lesson.question.comment.CommentService;
import com.example.elearningplatform.security.TokenUtil;

import lombok.Data;

@Service
@Data
public class LessonService {
    private final CommentService commentService;
    private final TokenUtil tokenUtil;
    private final LessonRepository lessonRepository;
    private final CommentRepository commentRepository;

    /*****************************************************************************************************/
    public LessonDto mapLessonToDto(Lesson lesson) {

        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(lesson.getId());
        lessonDto.setTitle(lesson.getTitle());
        lessonDto.setDuration(lesson.getDuration());
        lessonDto.setVideoUrl(lesson.getVideoUrl());

        return lessonDto;
    }

    /*****************************************************************************************************/
    public List<LessonDto> getLessonsBySectionId(Integer sectionId) {
        return lessonRepository.findBySectionId(sectionId).stream().map(this::mapLessonToDto).toList();
    }

    /*****************************************************************************************************/
    public LessonDto getLessonById(Integer id) {

        return mapLessonToDto(lessonRepository.findById(id).orElse(null));
    }
    /*****************************************************************************************************/

}
