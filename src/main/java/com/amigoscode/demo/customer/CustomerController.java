package com.amigoscode.demo.customer;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers/")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("getAllCustomers")
    public List<Customer> allCustomers(){
        return service.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomerById(@PathVariable("id") int id){
        return service.getCustomerById(id);
    }

    @PostMapping("addCustomer")
    public void registerCustomer(
            @RequestBody
            CustomerRegistrationRequest registrationRequest
    ){
        service.addCustomer(registrationRequest);
    }

    @DeleteMapping("deleteCustomerById/{id}")
    public void deleteCustomer(@PathVariable("id") int id){
        service.deleteCustomer(id);
    }

    @PutMapping("updateCustomer/{id}")
    public void updateCustomer(
            @PathVariable("id") Integer customerId,
            @RequestBody CustomerUpdateRequest updateRequest
    ){
        service.updateCustomer(customerId , updateRequest);
    }

}
