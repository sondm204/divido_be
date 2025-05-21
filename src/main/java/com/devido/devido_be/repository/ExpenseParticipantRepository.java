package com.devido.devido_be.repository;

import com.devido.devido_be.model.ExpenseParticipant;
import com.devido.devido_be.model.ExpenseParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, ExpenseParticipantId> {
}
