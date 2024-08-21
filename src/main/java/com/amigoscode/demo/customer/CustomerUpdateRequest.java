package com.amigoscode.demo.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
