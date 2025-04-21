package com.devido.devido_be.model;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GroupMemberId {
    private String groupId;
    private String userId;

    public GroupMemberId() {}
    public GroupMemberId(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMemberId that = (GroupMemberId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}
