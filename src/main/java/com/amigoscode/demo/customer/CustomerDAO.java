package com.amigoscode.demo.customer;

import java.util.List;
import java.util.Optional;


public interface CustomerDAO {

    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(int id);
    void insertCustomer(Customer customer);
    boolean existPersonWithEmail(String email);
    boolean existPersonWithId(int id);
    void deleteCustomerById(int id);
    void updateCustomer(Customer customer);
    Optional<Customer> selectUserByEmail(String email);
}
