package com.devido.devido_be.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupDTO {
    private String id;
    private String name;
    private Instant createdAt;
    private List<CategoryDTO> categories = new ArrayList<>();
    private List<ExpenseDTO> expenses = new ArrayList<>();
    private List<UserDTO> users = new ArrayList<>();

    public GroupDTO() {}

    public GroupDTO(String id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public GroupDTO(String id, String name, Instant createdAt, List<UserDTO> users) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.users = users;
    }

    public GroupDTO(String id, String name, Instant createdAt, List<CategoryDTO> categories, List<ExpenseDTO> expenses, List<UserDTO> users) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.categories = categories;
        this.expenses = expenses;
        this.users = users;
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

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }
}
