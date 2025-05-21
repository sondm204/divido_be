package com.devido.devido_be.service;

import com.devido.devido_be.dto.BillDTO;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.Bill;
import com.devido.devido_be.model.User;
import com.devido.devido_be.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
public class BillService {
    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public List<BillDTO> getAllBillsOfExpense(String expenseId) {
        List<Bill> bills = billRepository.findAllByExpenseId(expenseId);
        List<User> users = bills.stream().flatMap(b -> b.getUsers().stream()).sorted(Comparator.comparing(User::getName)).toList();
        return bills.stream().map(b -> new BillDTO(
                b.getId(),
                b.getName(),
                b.getQuantity(),
                b.getUnitPrice(),
                b.getTotalPrice(),
                users.stream().map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt())).toList()
        )).toList();
    }
}
