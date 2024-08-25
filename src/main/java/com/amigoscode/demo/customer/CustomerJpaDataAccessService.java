package com.amigoscode.demo.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("jpa")
public class CustomerJpaDataAccessService implements CustomerDAO{


    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository repository) {
        this.customerRepository = repository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(int id) {

        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existPersonWithId(int id) {
        return customerRepository.existsCustomerById(id);
    }

    @Override
    public void deleteCustomerById(int id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
