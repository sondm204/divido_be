package com.devido.devido_be.service;

import com.devido.devido_be.dto.*;
import com.devido.devido_be.model.*;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExpenseParticipantRepository expenseParticipantRepository;
    private final BillRepository billRepository;

    public ExpenseService(ExpenseRepository expenseRepository, GroupRepository groupRepository, CategoryRepository categoryRepository, UserRepository userRepository, ExpenseParticipantRepository expenseParticipantRepository, BillRepository billRepository) {
        this.expenseRepository = expenseRepository;
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.expenseParticipantRepository = expenseParticipantRepository;
        this.billRepository = billRepository;
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
                    UserDTO user = new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt());
                    expenseDTO.addShareRatio(user, ratio);
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

    public Expense createExpense(String groupId, ExpenseDTO expenseDTOs) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group with id " + groupId + " not found"));
        Category category = categoryRepository.findById(expenseDTOs.getCategory().getId()).orElseThrow(() -> new RuntimeException("Category with id " + expenseDTOs.getCategory().getId() + " not found"));
        User payer = userRepository.findById(expenseDTOs.getPayer().getId()).orElseThrow(() -> new RuntimeException("User with id " + expenseDTOs.getPayer().getId() + " not found"));
        String newExpenseId = UUIDGenerator.getRandomUUID();
        Expense newExpense = new Expense(
                newExpenseId,
                group,
                category,
                expenseDTOs.getAmount(),
                payer,
                expenseDTOs.getSpentAt(),
                expenseDTOs.getNote()
        );
        Expense savedExpense = expenseRepository.save(newExpense);
        if (expenseDTOs.getShareRatios() != null && !expenseDTOs.getShareRatios().isEmpty()) {
            List<ExpenseParticipant> expenseParticipants = new ArrayList<>();
            for (ShareRatio shareRatio : expenseDTOs.getShareRatios()) {
                User user = userRepository.findById(shareRatio.getUser().getId()).orElseThrow(() -> new RuntimeException("User with id " + shareRatio.getUser().getId() + " not found"));
                ExpenseParticipantId newExpenseParticipantId = new ExpenseParticipantId(savedExpense.getId(), user.getId());
                ExpenseParticipant expenseParticipant = new ExpenseParticipant(newExpenseParticipantId, savedExpense, user, shareRatio.getRatio());
                ExpenseParticipant savedExpenseParticipant = expenseParticipantRepository.save(expenseParticipant);
                expenseParticipants.add(savedExpenseParticipant);
            }
            Set<ExpenseParticipant> expenseParticipantSave = new HashSet<>(expenseParticipants);
            savedExpense.setExpenseParticipants(expenseParticipantSave);
        }

        if (expenseDTOs.getBills() != null && !expenseDTOs.getBills().isEmpty()) {
            List<Bill> bills = new ArrayList<>();
            for (BillDTO billDTO : expenseDTOs.getBills()) {
                String newBillId = UUIDGenerator.getRandomUUID();
                List<User> owner = billDTO.getOwner().stream().map(u -> userRepository.findById(u.getId()).orElseThrow(() -> new RuntimeException("User with id " + u.getId() + " not found"))).toList();
                Bill bill = new Bill(
                        newBillId,
                        savedExpense,
                        billDTO.getName(),
                        billDTO.getQuantity(),
                        billDTO.getUnitPrice(),
                        billDTO.getTotalPrice(),
                        new HashSet<>(owner)
                );
                Bill savedBill = billRepository.save(bill);
                bills.add(savedBill);
            }
            Set<Bill> billSave = new HashSet<>(bills);
            savedExpense.setBills(billSave);
        }
        return expenseRepository.findById(newExpenseId).orElseThrow(() -> new RuntimeException("Expense with id " + newExpenseId + " not found"));
    }

}
