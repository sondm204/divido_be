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
public class GroupMemberId implements java.io.Serializable {
    private static final long serialVersionUID = -300643088978000248L;
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "group_id", nullable = false, length = 50)
    private String groupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupMemberId entity = (GroupMemberId) o;
        return Objects.equals(this.groupId, entity.groupId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }

}