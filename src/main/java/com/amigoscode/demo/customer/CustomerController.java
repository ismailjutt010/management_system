package com.amigoscode.demo.customer;


import com.amigoscode.demo.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers/")
public class CustomerController {
    private final CustomerService service;

    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService service, JWTUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
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
    public ResponseEntity<?> registerCustomer(
            @RequestBody
            CustomerRegistrationRequest registrationRequest
    ){
        service.addCustomer(registrationRequest);
        String jwtToken =  jwtUtil.issueToken(registrationRequest.email(),"ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
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
