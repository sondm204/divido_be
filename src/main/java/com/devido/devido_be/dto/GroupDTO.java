package com.devido.devido_be.dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupDTO {
    private String id;
    private String name;
    private Instant createdAt;
    private List<UserDTO> users;

    public GroupDTO() {}

    public GroupDTO(String id, String name, Instant createdAt, List<UserDTO> users) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.users = users;
    }

    public GroupDTO(String id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
