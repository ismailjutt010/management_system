package com.amigoscode.demo.auth;

import com.amigoscode.demo.customer.Customer;
import com.amigoscode.demo.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {

}
