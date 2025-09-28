package com.devido.devido_be.service;

import com.devido.devido_be.dto.CategoryDTO;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.model.Category;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.CategoryRepository;
import com.devido.devido_be.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    private final List<String> defaultCategories = List.of("Ăn uống", "Di chuyển", "Mua sắm", "Đồ dùng cá nhân", "Giải trí", "Sức khỏe", "Học tập", "Nhà cửa", "Điện nước", "Thuê nhà", "Internet", "Điện thoại", "Khác");

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

    public List<CategoryDTO> createDefaultCategoriesForGroup(String groupId) {
        var group = groupRepository.findById(groupId);
        List<CategoryDTO> categories = new ArrayList<>();
        if (group.isEmpty()) {
            throw new RuntimeException("Group with id " + groupId + " not found");
        }
        for (String categoryName : defaultCategories) {
            var id = UUIDGenerator.getRandomUUID();
            categoryRepository.save(new Category(id, group.get(), categoryName));
            categories.add(new CategoryDTO(id, categoryName));
        }
        return categories;
    }

    public CategoryDTO updateCategory(String groupId, String categoryId, CategoryDTO categoryDTO) {
        var category = categoryRepository.findFirstByIdAndGroupId(categoryId, groupId);
        if (category == null) {
            throw new RuntimeException("Category with id " + categoryId + " not found");
        }
        category.setCategoryName(categoryDTO.getName());
        categoryRepository.save(category);
        return new CategoryDTO(category.getId(), category.getCategoryName());
    }

    public void deleteCategory(String groupId, String categoryId) {
        var category = categoryRepository.findFirstByIdAndGroupId(categoryId, groupId);
        if (category == null) {
            throw new RuntimeException("Category with id " + categoryId + " not found");
        }
        categoryRepository.delete(category);
    }
}
