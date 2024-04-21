package com.example.elearningplatform.course.section;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.lesson.LessonService;

import lombok.Data;


@Service
@Data
public class SectionService  {
    @Autowired private LessonService lessonService;
    public List<SectionDto>mapSectionToDto(List<Section>sections){
        List<SectionDto>sectionDtos = new ArrayList<>();

        sections.forEach(section -> {
            SectionDto sectionDto = new SectionDto();
            sectionDto.setId(section.getId());
            sectionDto.setTitle(section.getTitle());
            sectionDto.setDescription(section.getDescription());
            sectionDto.setDuration(section.getDuration());
            sectionDto.setLessons(lessonService.mapLessonsToDto(section.getLessons()));
            sectionDtos.add(sectionDto);

        });
  
        return sectionDtos;
       
        
    }


}
