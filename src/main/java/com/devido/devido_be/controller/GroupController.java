package com.devido.devido_be.controller;

import com.devido.devido_be.dto.*;
import com.devido.devido_be.model.*;
import com.devido.devido_be.service.CategoryService;
import com.devido.devido_be.service.ExpenseService;
import com.devido.devido_be.service.GroupService;
import com.devido.devido_be.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<?> getGroups() {
        return ResponseEntity.ok(groupService.getGroups());
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
            GroupDTO groupResponse = groupService.updateGroup(id, groupDTO);
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

    @PostMapping("/{id}/categories")
    public ResponseEntity<?> createCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO category = categoryService.createCategory(id, categoryDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Category created successfully", category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to create category", null));
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

    @PostMapping("/{id}/expenses")
    public ResponseEntity<?> createExpense(@PathVariable String id, @RequestBody ExpenseDTO expenseDTO) {
        try {
            Expense expense = expenseService.createExpense(id, expenseDTO);
            ExpenseDTO expenseResponse = new ExpenseDTO(
                    expense.getId(),
                    new CategoryDTO(expense.getCategory().getId(), expense.getCategory().getCategoryName()),
                    expense.getAmount(),
                    new UserDTO(expense.getPayer().getId(), expense.getPayer().getName(), expense.getPayer().getEmail(), expense.getPayer().getCreatedAt()),
                    expense.getSpentAt(),
                    expense.getNote(),
                    expense.getCreatedAt()
            );
            if (expense.getExpenseParticipants() != null && !expense.getExpenseParticipants().isEmpty()) {
                for(ExpenseParticipant expenseParticipant : expense.getExpenseParticipants()) {
                    User user = expenseParticipant.getUser();
                    expenseResponse.addShareRatio(
                            new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt()),
                            expenseParticipant.getShareRatio()
                    );
                }
            }
            if (expense.getBills() != null && !expense.getBills().isEmpty()) {
                for(Bill bill : expense.getBills()) {
                    Set<User> users = bill.getUsers();
                    expenseResponse.addBill(
                        new BillDTO(
                            bill.getId(),
                            bill.getName(),
                            bill.getQuantity(),
                            bill.getUnitPrice(),
                            bill.getTotalPrice(),
                            users.stream().map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt())).toList()
                        )
                    );
                }
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Expense created successfully", expenseResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to create expense", null));
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
