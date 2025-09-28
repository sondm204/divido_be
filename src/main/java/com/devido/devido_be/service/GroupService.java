package com.devido.devido_be.service;

import com.devido.devido_be.dto.*;
import com.devido.devido_be.model.*;
import com.devido.devido_be.other.UUIDGenerator;
import com.devido.devido_be.repository.GroupMemberRepository;
import com.devido.devido_be.repository.GroupRepository;
import com.devido.devido_be.repository.UserRepository;
import com.devido.devido_be.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final CategoryService categoryService;
    private final ExpenseService expenseService;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, GroupMemberRepository groupMemberRepository, CategoryService categoryService, ExpenseService expenseService) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.categoryService = categoryService;
        this.expenseService = expenseService;
    }

    public List<GroupDTO> getGroups() {
        var userId = SecurityUtils.getCurrentUserId();
        List<Group> groups = groupRepository.findAllByUserId(userId);
        List<GroupDTO> groupDTOs = new ArrayList<>();
        for (Group group : groups) {
            List<UserDTO> users = group.getGroupMembers().stream().map(u -> new UserDTO(u.getUser().getId(), u.getUser().getName(), u.getUser().getEmail(), u.getUser().getCreatedAt())).toList();
            groupDTOs.add(new GroupDTO(group.getId(), group.getName(), group.getCreatedAt(), users));
        }
        return groupDTOs;
    }

    public GroupDTO getGroupById(String id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        List<CategoryDTO> categories = categoryService.getAllCategoriesOfGroup(id);
        List<UserDTO> users = group.getGroupMembers().stream().map(u -> new UserDTO(u.getUser().getId(), u.getUser().getName(), u.getUser().getEmail(), u.getUser().getCreatedAt())).toList();
        List<ExpenseDTO> expenses = expenseService.getAllExpensesOfGroup(id);
        return new GroupDTO(group.getId(), group.getName(), group.getCreatedAt(), categories, expenses, users);
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

    public GroupDTO createGroup(GroupDTO groupDTO) {
        Group group = new Group();
        String newGroupId = UUIDGenerator.getRandomUUID();
        group.setId(newGroupId);
        groupDTO.setId(newGroupId);
        group.setName(groupDTO.getName());
        groupRepository.save(group);
        if (groupDTO.getUsers() != null) {
            for (UserDTO userDTO : groupDTO.getUsers()) {
                User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("User with id " + userDTO.getId() + " not found"));
                GroupMemberId groupMemberId = new GroupMemberId(groupDTO.getId(), userDTO.getId());
                GroupMember groupMember = new GroupMember(groupMemberId, group, user, Instant.now());
                groupMemberRepository.save(groupMember);
            }
        }
        var categories = categoryService.createDefaultCategoriesForGroup(newGroupId);
        groupDTO.setCategories(categories);
        return groupDTO;
    }

    @Transactional
    public GroupDTO updateGroup(String id, GroupDTO groupDTO) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        if (groupDTO.getName() != null) group.setName(groupDTO.getName());
        if (groupDTO.getUsers() != null) {
            groupMemberRepository.deleteAllByGroupId(id);
            for (UserDTO userDTO : groupDTO.getUsers()) {
                User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("User with id " + userDTO.getId() + " not found"));
                GroupMemberId groupMemberId = new GroupMemberId(id, userDTO.getId());
                GroupMember groupMember = new GroupMember(groupMemberId, group, user, Instant.now());
                groupMemberRepository.save(groupMember);
            }
        }
        for (CategoryDTO categoryDTO : groupDTO.getCategories()) {
            if (categoryDTO.getId() == null) {
                var newCategory = categoryService.createCategory(id, categoryDTO);
                categoryDTO.setId(newCategory.getId());
            } else {
                categoryService.updateCategory(id, categoryDTO.getId(), categoryDTO);
            }
        }
        groupRepository.save(group);
        return groupDTO;
    }

    public void deleteGroup(String id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found"));
        groupRepository.delete(group);
    }
}
