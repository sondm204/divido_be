package com.devido.devido_be.service;

import com.devido.devido_be.dto.CategoryDTO;
import com.devido.devido_be.dto.ExpenseDTO;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.*;
import com.devido.devido_be.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseDTO> getAllExpensesOfGroup(String groupId) {
        List<Expense> expenses = expenseRepository.findAllByGroupId(groupId);
        List<ExpenseDTO> expenseDTOs = new ArrayList<>();
        for (Expense e : expenses) {
            User p = e.getPayer();
            CategoryDTO category = new CategoryDTO(e.getCategory().getId(), e.getCategory().getCategoryName());
            UserDTO payer = new UserDTO(p.getId(), p.getName(), p.getEmail(), p.getCreatedAt());
            ExpenseDTO expenseDTO = new ExpenseDTO(e.getId(), category, e.getAmount(), payer, e.getSpentAt(), e.getNote(), e.getCreatedAt());
            if (e.getExpenseParticipants() != null && !e.getExpenseParticipants().isEmpty()) {
                List<ExpenseParticipant> sortedExpenseParticipants = e.getExpenseParticipants().stream()
                        .sorted(Comparator.comparing(
                                ep -> getJoinedAt(((ExpenseParticipant) ep).getUser(), groupId)
                        ))
                        .toList();

                for (ExpenseParticipant ep : sortedExpenseParticipants) {
                    User u = ep.getUser();
                    BigDecimal ratio = ep.getShareRatio();
                    expenseDTO.addShareRatio(u.getName(), ratio);
                }
            }
            expenseDTOs.add(expenseDTO);
        }
        return expenseDTOs;
    }

    private Instant getJoinedAt(User user, String groupId) {
        return user.getGroupMembers().stream()
                .filter(gm -> gm.getGroup().getId().equals(groupId))
                .findFirst()
                .map(GroupMember::getJoinedAt)
                .orElse(Instant.MIN); // hoặc throw nếu chắc chắn phải có
    }

}
