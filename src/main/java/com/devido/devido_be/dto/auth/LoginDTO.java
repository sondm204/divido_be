package com.devido.devido_be.dto.auth;

import lombok.*;

public class LoginDTO {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
