package com.example.elearningplatform.course.lesson;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.question.Question;
import com.example.elearningplatform.course.question.QuestionDto;
import com.example.elearningplatform.course.question.QuestionRepository;
import com.example.elearningplatform.security.TokenUtil;

import lombok.Data;


@Service
@Data
public class LessonService  {
    @Autowired private  QuestionRepository questionRepository;
    @Autowired private  TokenUtil tokenUtil;

/************************************************************************************************
*/
    public List<LessonDto> mapLessonsToDto(List<Lesson> lessons) {
        List<LessonDto> lessonDtos = new ArrayList<>();
        lessons.forEach(lesson -> {
            LessonDto lessonDto = new LessonDto();
            lessonDto.setId(lesson.getId());
            lessonDto.setTitle(lesson.getTitle());
            lessonDto.setDuration(lesson.getDuration());
            lessonDto.setVideoUrl(lesson.getVideoUrl());
            List<Question> votedQuestions = questionRepository.findByVote(tokenUtil.getUserId());
            votedQuestions.forEach(question -> {
                lessonDto.getQuestions().add(new QuestionDto(question, votedQuestions.contains(question)));
            });
            lessonDtos.add(lessonDto);
            
        });
        return lessonDtos;
    }
}
