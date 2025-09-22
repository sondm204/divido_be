package com.devido.devido_be.service;

import com.devido.devido_be.config.SecurityConfig;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.GroupMember;
import com.devido.devido_be.model.User;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.GroupRepository;
import com.devido.devido_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
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

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public List<UserDTO> getUsersOfGroup(String groupId) {
        List<User> users= groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group with id " + groupId + " not found")).getGroupMembers().stream().map(GroupMember::getUser).toList();
        return users.stream().map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt())).toList();
    }

    public User createUser(UserDTO user) {
        String uuid = UUIDGenerator.getRandomUUID();
        User newUser = new User(uuid, user.getName(), user.getEmail());
        userRepository.save(newUser);
        return userRepository.save(newUser);
    }

    public User updateUser(String id, UserDTO user) {
        User updateUser = userRepository.findById(id).orElse(null);
        assert updateUser != null;
        if (user.getName() != null) updateUser.setName(user.getName());
        if (user.getEmail() != null) updateUser.setEmail(user.getEmail());
        return userRepository.save(updateUser);
    }

    public void deleteUser(String id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        userRepository.deleteById(id);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
