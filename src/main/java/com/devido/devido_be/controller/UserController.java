package com.devido.devido_be.controller;

import com.devido.devido_be.dto.ApiResponse;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.User;
import com.devido.devido_be.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Cannot find user", null));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody UserDTO newUser) {
        try {
            User user = userService.createUser(newUser);
            UserDTO responseDTO = new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
            return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to create user", null));
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO newUser) {
        try {
            User user = userService.updateUser(newUser);
            UserDTO responseDTO = new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to update user", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Fail to delete user", null));
        }
    }
}
