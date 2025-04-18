package com.devido.devido_be.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ExpenseParticipantId implements java.io.Serializable {
    private static final long serialVersionUID = 5239408358437842618L;
    @Column(name = "expense_id", nullable = false, length = 50)
    private String expenseId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExpenseParticipantId entity = (ExpenseParticipantId) o;
        return Objects.equals(this.expenseId, entity.expenseId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenseId, userId);
    }

}