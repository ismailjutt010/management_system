package com.amigoscode.demo.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{

    static List<Customer> customers;
    static {
        customers = new ArrayList<>();

        Customer alex = new Customer(1,"Alex", "alex@gmail.com", "password", 15, Gender.MALE);
        Customer ismail = new Customer(2,"Ismail", "ismail@gmail.com", "password", 24, Gender.MALE);
        customers.add(alex);
        customers.add(ismail);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers ;
    }

    @Override
    public Optional<Customer> getCustomerById(int id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean existPersonWithId(int id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(int id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(o -> customers.remove(o));
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return customers.stream()
                .filter(customer -> customer.getUsername().equals(email))
                .findFirst();
    }
}
