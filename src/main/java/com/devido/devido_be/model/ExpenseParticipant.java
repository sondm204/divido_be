package com.devido.devido_be.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_participants")
public class ExpenseParticipant {
    @EmbeddedId
    private ExpenseParticipantId id;

    @ManyToOne
    @MapsId("expenseId")
    private Expense expense;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @Column(name = "share_ratio")
    private BigDecimal shareRatio;

    public ExpenseParticipant() {}

    public ExpenseParticipant(ExpenseParticipantId id, Expense expense, User user, BigDecimal shareRatio) {
        this.id = id;
        this.expense = expense;
        this.user = user;
        this.shareRatio = shareRatio;
    }

    public ExpenseParticipantId getId() {
        return id;
    }

    public void setId(ExpenseParticipantId id) {
        this.id = id;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getShareRatio() {
        return shareRatio;
    }

    public void setShareRatio(BigDecimal shareRatio) {
        this.shareRatio = shareRatio;
    }
}
