package com.devido.devido_be.service;

import com.devido.devido_be.dto.CategoryDTO;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.model.Category;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.CategoryRepository;
import com.devido.devido_be.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
