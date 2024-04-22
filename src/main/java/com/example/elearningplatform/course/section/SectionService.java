package com.example.elearningplatform.course.section;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonDto;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.lesson.LessonService;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Service
@Data
@RequiredArgsConstructor
public class SectionService {
    private final LessonService lessonService;

    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;

    /************************************************************************************** */

    public SectionDto mapSectionToDto(Section section) {
        if (section == null)
            return null;
            SectionDto sectionDto = new SectionDto();
            sectionDto.setId(section.getId());
            sectionDto.setTitle(section.getTitle());
            sectionDto.setDescription(section.getDescription());
            sectionDto.setDuration(section.getDuration());

            List<Lesson> sectionLessons = lessonRepository.findBySectionId(section.getId());
            sectionDto.setNumberOfSubComments(sectionLessons.size());
            List<LessonDto> sectionLessonDtos = new ArrayList<>();
            for (Lesson lesson : sectionLessons) {
                sectionLessonDtos.add(lessonService.mapLessonToDto(lesson));
                sectionDto.setLessons(sectionLessonDtos);
            }
     
        
    return sectionDto;

}

    public List<Section> getSectionsByCourseId(Integer courseID) {
        return sectionRepository.findByCourseId(courseID);
    }


}
