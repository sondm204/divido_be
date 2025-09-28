package com.devido.devido_be.repository;

import com.devido.devido_be.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, String> {
    @Query("SELECT g FROM Group g JOIN g.groupMembers gm WHERE gm.user.id = :userId ORDER BY g.createdAt DESC")
    List<Group> findAllByUserId(@Param("userId") String userId);
}
