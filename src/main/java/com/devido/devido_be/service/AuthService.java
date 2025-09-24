package com.devido.devido_be.service;

import com.devido.devido_be.config.SecurityConfig;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.dto.auth.LoginDTO;
import com.devido.devido_be.dto.auth.RegisterDTO;
import com.devido.devido_be.dto.auth.VerifyEmailRequest;
import com.devido.devido_be.model.User;
import com.devido.devido_be.model.VerificationCode;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.UserRepository;
import com.devido.devido_be.repository.VerificationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final SecurityConfig passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, SecurityConfig passwordEncoder, VerificationCodeRepository verificationCodeRepository, EmailService emailService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    public UserDTO login(LoginDTO request) {
        var user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.passwordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public UserDTO register(RegisterDTO request) {
        String token = request.getVerificationToken();
        if (token == null || !jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or expired verification token");
        }
        String emailFromToken = jwtService.getEmailFromToken(token);
        if (!emailFromToken.equals(request.getEmail())) {
            throw new IllegalArgumentException("Token does not match email");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        var user = new User();
        user.setId(UUIDGenerator.getRandomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.passwordEncoder().encode(request.getPassword()));
        user.setCreatedAt(Instant.now());
        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public void sendVerificationEmail(String email) {
        // Implementation for sending verification email
        if (verificationCodeRepository.findByEmail(email) != null) {
            verificationCodeRepository.deleteByEmail(email);
        }
        String code = String.valueOf((int) (Math.random() * 900000) + 100000);
        var entity = new VerificationCode(UUIDGenerator.getRandomUUID(), email, code, Instant.now().plusSeconds(300));
        verificationCodeRepository.save(entity);
        emailService.send(email, "Your verification code", "Your verification code is: " + code);
    }

    public String verifyEmail(VerifyEmailRequest request) {
        VerificationCode entity = verificationCodeRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!entity.getCode().equals(request.getCode())) {
            throw new RuntimeException("Invalid code");
        }
        if (entity.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Code expired");
        }

        verificationCodeRepository.delete(entity);
        return jwtService.generateEmailVerificationToken(request.getEmail());
    }
}