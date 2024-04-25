package com.example.elearningplatform.course.tag;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService{

    private final TagRepository tagRepository;
    /******************************************************************************* */


    public void createTag(Tag tag) {
        if(tag == null)return;
        tagRepository.save(tag);
    }


}
