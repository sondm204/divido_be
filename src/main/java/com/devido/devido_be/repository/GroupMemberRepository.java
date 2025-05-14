package com.devido.devido_be.repository;

import com.devido.devido_be.model.GroupMember;
import com.devido.devido_be.model.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    public void deleteAllByGroupId(String groupId);
}
