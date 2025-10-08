package com.devido.devido_be.controller;

import com.devido.devido_be.dto.*;
import com.devido.devido_be.model.*;
import com.devido.devido_be.service.BillService;
import com.devido.devido_be.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final BillService billService;

    public ExpenseController(ExpenseService expenseService, BillService billService) {
        this.expenseService = expenseService;
        this.billService = billService;
    }

    @GetMapping("/{id}/bill")
    public ResponseEntity<?> getAllBillsOfExpense(@PathVariable String id) {
        try {
            return ResponseEntity.ok(billService.getAllBillsOfExpense(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to get bills", null));
        }
    }

    @PostMapping("/{id}/bill")
    public ResponseEntity<?> createBill(@PathVariable String id, @RequestBody BillDTO billDTO) {
        try {
            Bill bill = billService.createBill(id, billDTO);
            BillDTO billResponse = new BillDTO(
                    bill.getId(),
                    bill.getName(),
                    bill.getQuantity(),
                    bill.getUnitPrice(),
                    bill.getTotalPrice(),
                    bill.getUsers().stream()
                            .map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt()))
                            .toList()
            );
            return ResponseEntity.ok(new ApiResponse<>(true, "Bill created successfully", billResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to create bill", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable String id, @RequestBody ExpenseDTO expenseDTO) {
        try {
            Expense expense = expenseService.updateExpense(id, expenseDTO);
            ExpenseDTO expenseResponse = new ExpenseDTO(
                    expense.getId(),
                    new CategoryDTO(expense.getCategory().getId(), expense.getCategory().getCategoryName()),
                    expense.getAmount(),
                    new UserDTO(expense.getPayer().getId(), expense.getPayer().getName(), expense.getPayer().getEmail(), expense.getPayer().getCreatedAt()),
                    expense.getSpentAt(),
                    expense.getNote(),
                    expense.getImageUrl(),
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
            return ResponseEntity.ok(new ApiResponse<>(true, "Expense updated successfully", expenseResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to update expense", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable String id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Expense deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to delete expense", null));
        }
    }
}
