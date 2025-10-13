package com.devido.devido_be.repository;

import com.devido.devido_be.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {
    @Query("SELECT e FROM Expense e WHERE e.group.id = :groupId ORDER BY e.spentAt DESC, e.createdAt DESC")
    List<Expense> findAllByGroupId(String groupId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.group.id = :groupId" )
    Long getTotalAmountByGroupId(String groupId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.payer.id = :userId" )
    Long getTotalAmountOfUser(String userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.group.id = :groupId AND e.payer.id = :userId" )
    Long getTotalAmountOfUserInGroupId(String groupId, String userId);
}
