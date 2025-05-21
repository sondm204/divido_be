package com.devido.devido_be.controller;

import com.devido.devido_be.dto.ApiResponse;
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
}
