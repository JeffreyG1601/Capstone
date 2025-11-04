package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.Customer;
import com.project1.networkinventory.enums.CustomerStatus;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findBySplitter_SplitterId(Long splitterId);

    List<Customer> findByStatus(CustomerStatus status);

    // ✅ Added safe port check method
    boolean existsBySplitter_SplitterIdAndAssignedPort(Long splitterId, Integer assignedPort);
}
