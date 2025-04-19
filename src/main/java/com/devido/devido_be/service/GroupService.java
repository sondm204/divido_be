package com.devido.devido_be.service;

import com.devido.devido_be.dto.ApiResponse;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.Group;
import com.devido.devido_be.model.User;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.GroupRepository;
import com.devido.devido_be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<GroupDTO> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        List<GroupDTO> groupDTOs = new ArrayList<>();
        for (Group group : groups) {
            List<UserDTO> users = group.getUsers().stream().map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt())).toList();
            groupDTOs.add(new GroupDTO(group.getId(), group.getName(), group.getCreatedAt(), users));
        }
        return groupDTOs;
    }

    public GroupDTO getGroupById(String id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        List<UserDTO> users = group.getUsers().stream().map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt())).toList();
        return new GroupDTO(group.getId(), group.getName(), group.getCreatedAt(), users);
    }

    public Group createGroup(GroupDTO groupDTO) {
        Group group = new Group();
        group.setId(UUIDGenerator.getRandomUUID());
        group.setName(groupDTO.getName());
        return groupRepository.save(group);
    }

    public Group updateGroup(String id, GroupDTO groupDTO) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        if(groupDTO.getName() != null) group.setName(groupDTO.getName());
        if(groupDTO.getUsers() != null) {
            Set<User> users = new HashSet<>();
            for (UserDTO userDTO : groupDTO.getUsers()) {
                User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("User with id " + userDTO.getId() + " not found"));
                users.add(user);
            }
            for(User user : users) {
                user.getGroups().add(group);
            }
            group.setUsers(users);
        }
        return groupRepository.save(group);
    }

    public void deleteGroup(String id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        groupRepository.delete(group);
    }
}
