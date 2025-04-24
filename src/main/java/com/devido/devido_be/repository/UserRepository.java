package com.devido.devido_be.repository;

import com.devido.devido_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    public User findByEmail(String email);
}
