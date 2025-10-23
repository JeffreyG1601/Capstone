package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.Customer;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find customers by splitter ID
    List<Customer> findBySplitterSplitterId(Long splitterId);

    // Other existing methods
    List<Customer> findByStatus(String status);
}
