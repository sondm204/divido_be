package com.devido.devido_be.controller;

import com.devido.devido_be.dto.*;
import com.devido.devido_be.model.Bill;
import com.devido.devido_be.model.Category;
import com.devido.devido_be.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO category = categoryService.createCategory(id, categoryDTO);
            CategoryDTO categoryResponse = new CategoryDTO(category.getId(), category.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Category created successfully", categoryResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to create Category", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        try {
            Category categoryResponse = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Category updated successfully", categoryResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to update Category", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        try {
            categoryService.deleteeCategory(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to delete Category", null));
        }
    }
}
