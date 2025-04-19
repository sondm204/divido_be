package com.devido.devido_be.service;

import com.devido.devido_be.dto.CategoryDTO;
import com.devido.devido_be.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategoriesOfGroup(String groupId) {
        return categoryRepository.findAllByGroupId(groupId).stream().map(c -> new CategoryDTO(c.getId(), c.getCategoryName())).toList();
    }
}
