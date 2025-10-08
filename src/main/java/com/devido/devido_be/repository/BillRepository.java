package com.devido.devido_be.repository;

import com.devido.devido_be.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, String> {
    @Query("SELECT b FROM Bill b WHERE b.expense.id = :expenseId ORDER BY b.createdAt DESC")
    public List<Bill> findAllByExpenseId(String expenseId);
}
