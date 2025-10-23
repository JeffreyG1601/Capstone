package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.Customer;
import com.project1.networkinventory.repository.CustomerRepository;
import com.project1.networkinventory.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    // Remove = null — let Spring inject via constructor
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
		super();
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
    public Customer getCustomerById(Long user_id) {
        return customerRepository.findById(user_id).orElse(null);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        customer.setCustomerId(id);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
