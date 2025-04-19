package com.devido.devido_be.repository;

import com.devido.devido_be.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    public List<Category> findAllByGroupId(String groupId);
}
