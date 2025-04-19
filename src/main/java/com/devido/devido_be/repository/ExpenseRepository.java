package com.devido.devido_be.repository;

import com.devido.devido_be.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {
    public List<Expense> findAllByGroupId(String groupId);
}
