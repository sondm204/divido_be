package com.devido.devido_be.service;

import com.devido.devido_be.dto.CategoryDTO;
import com.devido.devido_be.dto.ExpenseDTO;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.dto.ShareRatio;
import com.devido.devido_be.model.*;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.CategoryRepository;
import com.devido.devido_be.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    public CategoryService(CategoryRepository categoryRepository, GroupRepository groupRepository) {
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
    }

    public List<CategoryDTO> getAllCategoriesOfGroup(String groupId) {
        return categoryRepository.findAllByGroupId(groupId).stream().map(c -> new CategoryDTO(c.getId(), c.getCategoryName())).toList();
    }

    public CategoryDTO createCategory(String groupId, CategoryDTO categoryDTO) {
        var group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new RuntimeException("Group with id " + groupId + " not found");
        }
        var id = UUIDGenerator.getRandomUUID();
        var category = categoryRepository.save(new Category(id, group.get(), categoryDTO.getName()));
        return new CategoryDTO(category.getId(), category.getCategoryName());
    }

    public Category updateCategory(String id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id " + categoryDTO.getId() + " not found"));
        category.setCategoryName(categoryDTO.getName());
        Category updateCategory = categoryRepository.save(category);
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id " + id + " not found"));
    }

    public void deleteeCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id " + id + " not found"));
        categoryRepository.delete(category);
    }
}
