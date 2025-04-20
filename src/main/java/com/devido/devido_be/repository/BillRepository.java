package com.devido.devido_be.repository;

import com.devido.devido_be.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, String> {
    public List<Bill> findAllByExpenseId(String expenseId);
}
