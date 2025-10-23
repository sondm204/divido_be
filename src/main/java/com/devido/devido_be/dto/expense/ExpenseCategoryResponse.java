package com.devido.devido_be.dto.expense;

public class ExpenseCategoryResponse {
    private String categoryName;
    private long total;
    private int percentage;
    public ExpenseCategoryResponse() {}
    public ExpenseCategoryResponse(String categoryName, long total) {
        this.categoryName = categoryName;
        this.total = total;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
