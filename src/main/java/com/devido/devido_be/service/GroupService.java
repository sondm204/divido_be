package com.devido.devido_be.service;

import com.devido.devido_be.dto.ApiResponse;
import com.devido.devido_be.dto.GroupDTO;
import com.devido.devido_be.dto.UserDTO;
import com.devido.devido_be.model.Group;
import com.devido.devido_be.model.GroupMember;
import com.devido.devido_be.model.GroupMemberId;
import com.devido.devido_be.model.User;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.GroupRepository;
import com.devido.devido_be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
            List<UserDTO> users = group.getGroupMembers().stream().map(u -> new UserDTO(u.getUser().getId(), u.getUser().getName(), u.getUser().getEmail(), u.getUser().getCreatedAt())).toList();
            groupDTOs.add(new GroupDTO(group.getId(), group.getName(), group.getCreatedAt(), users));
        }
        return groupDTOs;
    }

    public GroupDTO getGroupById(String id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        List<UserDTO> users = group.getGroupMembers().stream().map(u -> new UserDTO(u.getUser().getId(), u.getUser().getName(), u.getUser().getEmail(), u.getUser().getCreatedAt())).toList();
        return new GroupDTO(group.getId(), group.getName(), group.getCreatedAt(), users);
    }

    public List<GroupDTO> getAllGroupsOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
        List<Group> groups = user.getGroupMembers().stream().map(GroupMember::getGroup).toList();
        List<Group> sortedGroups = groups.stream().sorted((g1, g2) -> g2.getCreatedAt().compareTo(g1.getCreatedAt())).toList();
        List<GroupDTO> groupDTOs = new ArrayList<>();
        for (Group group : sortedGroups) {
            groupDTOs.add(new GroupDTO(group.getId(), group.getName(), group.getCreatedAt()));
        }
        return groupDTOs;
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
                List<Group> groups = new ArrayList<>(user.getGroupMembers().stream().map(GroupMember::getGroup).toList());
                groups.add(group);
            }
            group.setGroupMembers(users.stream().map(u -> new GroupMember(new GroupMemberId(id, u.getId()), group, u, Instant.now())).collect(Collectors.toSet()));
        }
        return groupRepository.save(group);
    }

    public void deleteGroup(String id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        groupRepository.delete(group);
    }
}
