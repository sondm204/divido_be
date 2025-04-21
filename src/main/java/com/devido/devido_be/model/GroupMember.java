package com.devido.devido_be.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "group_members")
public class GroupMember {
    @EmbeddedId
    private GroupMemberId id;

    @ManyToOne
    @MapsId("groupId")
    private Group group;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @Column(name = "joined_at")
    private Instant joinedAt;

    public GroupMember() {}

    public GroupMember(GroupMemberId id, Group group, User user, Instant joinedAt) {
        this.id = id;
        this.group = group;
        this.user = user;
        this.joinedAt = joinedAt;
    }

    public GroupMemberId getId() {
        return id;
    }

    public void setId(GroupMemberId id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }
}
