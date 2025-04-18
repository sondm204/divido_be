package com.devido.devido_be.repository;

import com.devido.devido_be.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, String> {
}
