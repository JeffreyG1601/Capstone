package com.project1.networkinventory.service;

import com.project1.networkinventory.model.Customer;
import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long user_id);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
}
