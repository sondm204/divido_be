package com.devido.devido_be.controller;

import com.devido.devido_be.dto.ApiResponse;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.model.Group;
import com.devido.devido_be.service.CategoryService;
import com.devido.devido_be.service.ExpenseService;
import com.devido.devido_be.service.GroupService;
import com.devido.devido_be.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final CategoryService categoryService;
    private final ExpenseService expenseService;
    private final UserService userService;

    public GroupController(GroupService groupService, CategoryService categoryService, ExpenseService expenseService, UserService userService) {
        this.groupService = groupService;
        this.categoryService = categoryService;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(groupService.getGroupById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Group with id " + id + " not found", null));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            Group group = groupService.createGroup(groupDTO);
            GroupDTO groupResponse = new GroupDTO(group.getId(), group.getName(), group.getCreatedAt());
            return ResponseEntity.ok(new ApiResponse<>(true, "Group created successfully", groupResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to create group", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable String id, @RequestBody GroupDTO groupDTO) {
        try {
            Group group = groupService.updateGroup(id, groupDTO);
            GroupDTO groupResponse = new GroupDTO(group.getId(), group.getName(), group.getCreatedAt());
            return ResponseEntity.ok(new ApiResponse<>(true, "Group updated successfully", groupResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to update group", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        try {
            groupService.deleteGroup(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Group deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to delete group", null));
        }
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<?> getAllCategories(@PathVariable String id) {
        try {
        return ResponseEntity.ok(categoryService.getAllCategoriesOfGroup(id));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to get categories", null));
        }
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<?> getAllExpenses(@PathVariable String id) {
        try {
            return ResponseEntity.ok(expenseService.getAllExpensesOfGroup(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to get expenses", null));
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getAllUsers(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUsersOfGroup(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to get users", null));
        }
    }
}
