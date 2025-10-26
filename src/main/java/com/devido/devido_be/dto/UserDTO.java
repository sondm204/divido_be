package com.devido.devido_be.dto;


import java.time.Instant;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private Instant createdAt;
    private Long totalBudget;
    private Long foodBudget;
    private Long entertainmentBudget;
    private Boolean isReminded;

    public UserDTO() {}
    public UserDTO(String id, String name, String email, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UserDTO(String id, String name, String email, Instant createdAt, Long totalBudget, Long foodBudget, Long entertainmentBudget, Boolean isReminded) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.totalBudget = totalBudget;
        this.foodBudget = foodBudget;
        this.entertainmentBudget = entertainmentBudget;
        this.isReminded = isReminded;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Long totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Long getFoodBudget() {
        return foodBudget;
    }

    public void setFoodBudget(Long foodBudget) {
        this.foodBudget = foodBudget;
    }

    public Long getEntertainmentBudget() {
        return entertainmentBudget;
    }

    public void setEntertainmentBudget(Long entertainmentBudget) {
        this.entertainmentBudget = entertainmentBudget;
    }

    public Boolean getReminded() {
        return isReminded;
    }

    public void setReminded(Boolean reminded) {
        isReminded = reminded;
    }
}
