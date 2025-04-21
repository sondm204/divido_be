package com.devido.devido_be.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ExpenseParticipantId implements Serializable {
    private String expenseId;
    private String userId;

    public ExpenseParticipantId() {}

    public ExpenseParticipantId(String expenseId, String userId) {
        this.expenseId = expenseId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseParticipantId that = (ExpenseParticipantId) o;
        return Objects.equals(expenseId, that.expenseId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenseId, userId);
    }
}
