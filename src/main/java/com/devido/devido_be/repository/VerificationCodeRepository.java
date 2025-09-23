package com.devido.devido_be.repository;

import com.devido.devido_be.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    Optional<VerificationCode> findByEmail(String email);
    void deleteByEmail(String email);
}
