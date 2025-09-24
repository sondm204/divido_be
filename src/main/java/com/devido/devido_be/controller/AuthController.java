package com.devido.devido_be.controller;

import com.devido.devido_be.dto.ApiResponse;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.dto.auth.EmailRequest;
import com.devido.devido_be.dto.auth.LoginDTO;
import com.devido.devido_be.dto.auth.RegisterDTO;
import com.devido.devido_be.dto.auth.VerifyEmailRequest;
import com.devido.devido_be.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            var response = authService.login(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Login failed", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) {
        try {
            var user = authService.register(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Register successful", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Register failed", null));
        }
    }

    @PostMapping("/email-verification")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody EmailRequest request) {
        try {
            authService.sendVerificationEmail(request.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "Verification email sent", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Failed to send verification email", null));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        try {
            var jwt = authService.verifyEmail(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Email verified", jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Failed to verify email", null));
        }
    }
}
