package com.amigoscode.demo.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
