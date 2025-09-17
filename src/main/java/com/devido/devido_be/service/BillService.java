package com.devido.devido_be.service;

import com.devido.devido_be.dto.BillDTO;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.Bill;
import com.devido.devido_be.model.Expense;
import com.devido.devido_be.model.User;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.BillRepository;
import com.devido.devido_be.repository.ExpenseRepository;
import com.devido.devido_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public BillService(BillRepository billRepository, ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.billRepository = billRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public List<BillDTO> getAllBillsOfExpense(String expenseId) {
        List<Bill> bills = billRepository.findAllByExpenseId(expenseId);
        bills.forEach(bill ->
                bill.setUsers(
                        bill.getUsers().stream()
                                .sorted(Comparator.comparing(User::getName))
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                )
        );
        return bills.stream().map(b -> new BillDTO(
                b.getId(),
                b.getName(),
                b.getQuantity(),
                b.getUnitPrice(),
                b.getTotalPrice(),
                b.getUsers().stream()
                        .map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt()))
                        .collect(Collectors.toList())
        )).toList();
    }

    public Bill createBill(String expenseId, BillDTO billDTO) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        List<UserDTO> owner = billDTO.getOwner();
        if(owner == null || owner.isEmpty()) {
            throw new RuntimeException("Bill must have at least one owner");
        }
        Set<User> users = owner.stream().map(u -> userRepository.findById(u.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        String billId = UUIDGenerator.getRandomUUID();
        Bill bill = new Bill(
                billId,
                expense,
                billDTO.getName(),
                billDTO.getQuantity(),
                billDTO.getUnitPrice(),
                billDTO.getTotalPrice(),
                users
        );
        return billRepository.save(bill);
    }

    public Bill updateBill(String billId, BillDTO billDTO) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        List<UserDTO> owner = billDTO.getOwner();
        if(owner == null || owner.isEmpty()) {
            throw new RuntimeException("Bill must have at least one owner");
        }
        Set<User> users = owner.stream().map(u -> userRepository.findById(u.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        bill.setName(billDTO.getName());
        bill.setQuantity(billDTO.getQuantity());
        bill.setUnitPrice(billDTO.getUnitPrice());
        bill.setTotalPrice(billDTO.getTotalPrice());
        bill.setUsers(users);
        return billRepository.save(bill);
    }

    public void deleteBill(String billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        billRepository.delete(bill);
    }

    public void deleteMultipleBills(String billIds) {
        String[] ids = billIds.split(",");
        List<Bill> bills = billRepository.findAllById(Arrays.asList(ids));
        if(bills.size() != ids.length) {
            throw new RuntimeException("Some bills not found");
        }
        billRepository.deleteAll(bills);
    }
}
