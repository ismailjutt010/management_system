package com.amigoscode.demo.customer;

import com.amigoscode.demo.exceptions.DuplicateResourceException;
import com.amigoscode.demo.exceptions.RequestValidationException;
import com.amigoscode.demo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO dao) {
        this.customerDAO = dao;
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.getAllCustomers();
    }

    public Customer getCustomerById(int id){
        return customerDAO.getCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        if(customerDAO.existPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email Already Taken");
        }

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomer(int id){
        if(!customerDAO.existPersonWithId(id)){
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(id));
        }
        customerDAO.deleteCustomerById(id);
    }

    public void updateCustomer(Integer customerId , CustomerUpdateRequest updateRequest){
        Customer customer = getCustomerById(customerId);
        boolean changes = false;

        if(updateRequest.name() !=null && !customer.getName().equalsIgnoreCase(updateRequest.name())){
            customer.setName(updateRequest.name());
            customerDAO.updateCustomer(customer);
            changes = true;
        }

        if(updateRequest.email() !=null && !customer.getEmail().equalsIgnoreCase(updateRequest.email())){
            if(customerDAO.existPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException("Email Already Taken");
            }

            customer.setEmail(updateRequest.email());
            customerDAO.updateCustomer(customer);
            changes = true;
        }

        if(updateRequest.age() !=null && !customer.getAge().equals(updateRequest.age())){
            customer.setAge(updateRequest.age());
            customerDAO.updateCustomer(customer);
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no data changes found");
        }
    }

}
