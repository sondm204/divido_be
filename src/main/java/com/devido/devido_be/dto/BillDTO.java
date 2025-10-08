package com.devido.devido_be.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class BillDTO {
    private String id;
    private String name;
    private BigDecimal quantity;
    private Integer unitPrice;
    private Integer totalPrice;
    private List<UserDTO> owner;
    private Instant createdAt;

    public BillDTO() {}

    public BillDTO(String id, String name, BigDecimal quantity, Integer unitPrice, Integer totalPrice, List<UserDTO> owner, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.owner = owner;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<UserDTO> getOwner() {
        return owner;
    }

    public void setOwner(List<UserDTO> owner) {
        this.owner = owner;
    }

    public void addOwner(UserDTO user) {
        if (this.owner == null) this.owner = List.of(user);
        this.owner.add(user);
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
}
