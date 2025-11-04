package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.enums.CustomerStatus;
import com.project1.networkinventory.model.Customer;
import com.project1.networkinventory.repository.CustomerRepository;
import com.project1.networkinventory.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    // explicit constructor (keeps compatibility with your memory snapshots)
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    /**
     * Merge-update. Only update allowed scalar fields and preserve asset relations.
     */
    @Override
    @Transactional
    public Customer updateCustomer(Long id, Customer incoming) {
        if (incoming == null) throw new IllegalArgumentException("Customer payload required");

        Customer existing = customerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));

        // Updatable scalar fields only
        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getAddress() != null) existing.setAddress(incoming.getAddress());
        if (incoming.getNeighborhood() != null) existing.setNeighborhood(incoming.getNeighborhood());
        if (incoming.getPlan() != null) existing.setPlan(incoming.getPlan());
        if (incoming.getConnectionType() != null) existing.setConnectionType(incoming.getConnectionType());
        if (incoming.getStatus() != null) existing.setStatus(incoming.getStatus());
        if (incoming.getSplitter() != null) existing.setSplitter(incoming.getSplitter());
        if (incoming.getAssignedPort() != null) existing.setAssignedPort(incoming.getAssignedPort());

        // Preserve assigned assets. Do not copy asset fields from incoming.
        return customerRepository.save(existing);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getCustomersByStatus(CustomerStatus status) {
        return customerRepository.findByStatus(status);
    }
}
