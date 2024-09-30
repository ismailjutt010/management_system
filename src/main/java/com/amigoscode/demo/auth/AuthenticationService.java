package com.amigoscode.demo.auth;

import com.amigoscode.demo.customer.Customer;
import com.amigoscode.demo.customer.CustomerDTO;
import com.amigoscode.demo.customer.CustomerDTOMapper;
import com.amigoscode.demo.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, CustomerDTOMapper customerDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.userName(),
                        request.password()
                )
        );
        Customer principal =(Customer) authenticate.getPrincipal();
        CustomerDTO cDto = customerDTOMapper.apply(principal);
        String token = jwtUtil.issueToken(cDto.userName() , cDto.roles());
        return new AuthenticationResponse(
                token , cDto
        );
    }
}
