package com.example.elearningplatform.course.section;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.Setter;


@Service
@Setter
@Transactional
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    /************************************************************************************** */


    public List<Section> getSectionsByCourseId(Integer courseID) {
        return sectionRepository.findByCourseId(courseID);
    }


}
