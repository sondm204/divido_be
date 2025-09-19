package com.devido.devido_be.repository;

import com.devido.devido_be.model.ExpenseParticipant;
import com.devido.devido_be.model.ExpenseParticipantId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, ExpenseParticipantId> {
    List<ExpenseParticipant> findByExpense_Id(String expenseId);

    @Modifying
    @Transactional
    void deleteByExpense_Id(String expenseId);
}
