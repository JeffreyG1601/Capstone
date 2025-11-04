package com.project1.networkinventory.controller;

import com.project1.networkinventory.enums.CustomerStatus;
import com.project1.networkinventory.model.Customer;
import com.project1.networkinventory.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    // ✅ Added endpoint for frontend “pending customers” tab
    @GetMapping("/pending")
    public List<Customer> getPendingCustomers() {
        return customerService.getCustomersByStatus(CustomerStatus.PENDING);
    }
}
