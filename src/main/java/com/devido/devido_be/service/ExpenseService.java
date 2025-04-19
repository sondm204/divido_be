package com.devido.devido_be.service;

import com.devido.devido_be.dto.CategoryDTO;
import com.devido.devido_be.dto.ExpenseDTO;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.Expense;
import com.devido.devido_be.model.Group;
import com.devido.devido_be.model.User;
import com.devido.devido_be.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseDTO> getAllExpensesOfGroup(String groupId) {
        List<Expense> expenses = expenseRepository.findAllByGroupId(groupId);
        List<ExpenseDTO> expenseDTOs = new ArrayList<>();
        for(Expense e : expenses) {
            User p = e.getPayer();
            CategoryDTO category = new CategoryDTO(e.getCategory().getId(), e.getCategory().getCategoryName());
            UserDTO payer = new UserDTO(p.getId(), p.getName(), p.getEmail(), p.getCreatedAt());
            expenseDTOs.add(new ExpenseDTO(e.getId(), category, e.getAmount(), payer, e.getSpentAt(), e.getNote(), e.getCreatedAt()));
        }
        return expenseDTOs;
    }
}
