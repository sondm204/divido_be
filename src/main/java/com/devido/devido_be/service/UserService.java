package com.devido.devido_be.service;

import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.User;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(u -> new UserDTO(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt()
        )).toList();
    }

    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public User createUser(UserDTO user) {
        String uuid = UUIDGenerator.getRandomUUID();
        User newUser = new User(uuid, user.getName(), user.getEmail());
        userRepository.save(newUser);
        return userRepository.save(newUser);
    }

    public User updateUser(UserDTO user) {
        User updateUser = userRepository.findById(user.getId()).orElse(null);
        assert updateUser != null;
        if (user.getName() != null) updateUser.setName(user.getName());
        if (user.getEmail() != null) updateUser.setEmail(user.getEmail());
        return userRepository.save(updateUser);
    }

    public String deleteUser(String id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        userRepository.deleteById(id);
        return id;
    }
}
