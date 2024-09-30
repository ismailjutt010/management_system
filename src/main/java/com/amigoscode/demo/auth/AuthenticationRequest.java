package com.amigoscode.demo.auth;

public record AuthenticationRequest(
        String userName,
        String password
) {
}
