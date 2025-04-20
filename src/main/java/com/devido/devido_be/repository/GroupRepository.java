package com.devido.devido_be.repository;

import com.devido.devido_be.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, String> {
}
