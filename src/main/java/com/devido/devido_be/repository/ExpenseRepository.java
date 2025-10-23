package com.devido.devido_be.repository;

import com.devido.devido_be.dto.expense.ExpenseCategoryResponse;
import com.devido.devido_be.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {
    @Query("SELECT e FROM Expense e WHERE e.group.id = :groupId AND MONTH(e.spentAt) = :month AND YEAR(e.spentAt) = :year ORDER BY e.spentAt DESC, e.createdAt DESC")
    List<Expense> findAllByGroupId(String groupId, int month, int year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.group.id = :groupId AND MONTH(e.spentAt) = :month AND YEAR(e.spentAt) = :year")
    Long getTotalAmountByGroupId(String groupId, int month, int year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.payer.id = :userId AND MONTH(e.spentAt) = :month AND YEAR(e.spentAt) = :year")
    Long getTotalAmountOfUser(String userId, int month, int year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.group.id = :groupId AND e.payer.id = :userId AND MONTH(e.spentAt) = :month AND YEAR(e.spentAt) = :year")
    Long getTotalAmountOfUserInGroupId(String groupId, String userId, int month, int year);

    @Query("""
           SELECT new com.devido.devido_be.dto.expense.ExpenseCategoryResponse(
               e.category.categoryName,
               COALESCE(SUM(e.amount), 0)
           )
           FROM Expense e
           WHERE e.payer.id = :userId
             AND MONTH(e.spentAt) = :month
             AND YEAR(e.spentAt) = :year
           GROUP BY e.category.categoryName
           ORDER BY SUM(e.amount) DESC
           LIMIT 3
           """)
    List<ExpenseCategoryResponse> getTotalAmountOfCategory(String userId, int month, int year);

}
