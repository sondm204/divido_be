package com.devido.devido_be.dto;

import com.devido.devido_be.model.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ExpenseDTO {
    private String id;
    private CategoryDTO category;
    private Integer amount;
    private UserDTO payer;
    private LocalDate spentAt;
    private String note;
    private List<ShareRatio> shareRatios = new ArrayList<>();
    private List<BillDTO> bills = new ArrayList<>();
    private Instant createdAt;

    public ExpenseDTO() {}

    public ExpenseDTO(String id, CategoryDTO categories, Integer amount, UserDTO payer, LocalDate spentAt, String note, Instant createdAt) {
        this.id = id;
        this.category = categories;
        this.amount = amount;
        this.payer = payer;
        this.spentAt = spentAt;
        this.note = note;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public UserDTO getPayer() {
        return payer;
    }

    public void setPayer(UserDTO payer) {
        this.payer = payer;
    }

    public LocalDate getSpentAt() {
        return spentAt;
    }

    public void setSpentAt(LocalDate spentAt) {
        this.spentAt = spentAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<ShareRatio> getShareRatios() {
        return shareRatios;
    }

    public void setShareRatios(List<ShareRatio> shareRatios) {
        this.shareRatios = shareRatios;
    }

    public void addShareRatio(UserDTO user, BigDecimal shareRatio) {
        this.shareRatios.add(new ShareRatio(user, shareRatio));
    }

    public List<BillDTO> getBills() {
        return bills;
    }

    public void setBills(List<BillDTO> bills) {
        this.bills = bills;
    }

    public void addBill(BillDTO bill) {
        if (this.bills == null) this.bills = new ArrayList<>();
        this.bills.add(bill);
    }
}
