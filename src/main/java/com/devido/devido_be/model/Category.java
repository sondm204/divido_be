package com.devido.devido_be.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "category_name", length = 50)
    private String categoryName;

    public Category() {
    }

    public Category(String id, Group group, String categoryName) {
        this.id = id;
        this.group = group;
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}