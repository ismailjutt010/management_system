package com.amigoscode.demo.customer;

import java.util.List;
import java.util.Optional;


public interface CustomerDAO {

    public List<Customer> getAllCustomers();
    public Optional<Customer> getCustomerById(int id);
    void insertCustomer(Customer customer);
    boolean existPersonWithEmail(String email);
    boolean existPersonWithId(int id);
    void deleteCustomerById(int id);
    void updateCustomer(Customer customer);
}
