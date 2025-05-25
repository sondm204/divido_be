package com.devido.devido_be.controller;

import com.devido.devido_be.dto.ApiResponse;
import com.devido.devido_be.dto.BillDTO;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.Bill;
import com.devido.devido_be.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBill(@PathVariable String id, @RequestBody BillDTO billDTO) {
        try {
            Bill bill = billService.updateBill(id, billDTO);
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
            return ResponseEntity.ok(new ApiResponse<>(true, "Bill updated successfully", billResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to update bill", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable String id) {
        try {
            billService.deleteBill(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Bill deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to delete bill", null));
        }
    }
}
