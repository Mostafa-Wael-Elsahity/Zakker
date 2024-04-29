package com.example.elearningplatform.course.category;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService  {
    private final CategoryRepository categoryRepository;

    /***********************************************************************************************/
    public List<Category> findAll() {
        return  categoryRepository.findAll();
    }

    /***********************************************************************************************/
    
    public void createCategory(Category category) {
        if(category == null)return;
        categoryRepository.save(category);
    }

    /***********************************************************************************************/

}
