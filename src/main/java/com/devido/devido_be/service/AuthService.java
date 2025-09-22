package com.devido.devido_be.service;

import com.devido.devido_be.config.SecurityConfig;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.dto.auth.LoginDTO;
import com.devido.devido_be.dto.auth.RegisterDTO;
import com.devido.devido_be.model.User;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
    private final SecurityConfig passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository, SecurityConfig passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO login(LoginDTO request) {
        var user = userRepository.findByEmail(request.getEmail());
        if(user == null) {
            throw new RuntimeException("User not found");
        }
        if(!passwordEncoder.passwordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public UserDTO register(RegisterDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
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
}