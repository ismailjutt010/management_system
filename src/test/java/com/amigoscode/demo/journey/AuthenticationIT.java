package com.amigoscode.demo.journey;
import com.amigoscode.demo.auth.AuthenticationRequest;
import com.amigoscode.demo.auth.AuthenticationResponse;
import com.amigoscode.demo.customer.CustomerDTO;
import com.amigoscode.demo.customer.CustomerRegistrationRequest;
import com.amigoscode.demo.customer.Gender;
import com.amigoscode.demo.jwt.JWTUtil;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.Random;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIT {

    private static final Random RANDOM = new Random();
    @Autowired
    private WebTestClient webTestClient;
    private static final String AUTHENTICATION_PATH = "api/v1/auth";
    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void canLogin () {
        // Given

        //create a registration customerRegistrationRequest
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@testenvironment.com";
        int age = RANDOM.nextInt(1, 100);

        Gender gender = age % 2 == 0 ?Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                "password", age,
                gender
        );
        String password = "password";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                email,
                password
        );

        webTestClient.post()
                .uri(AUTHENTICATION_PATH+"/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest) , AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();


        webTestClient.post()
                .uri("/api/v1/customers/addCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                }).returnResult();

        String jwtToken = result.getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        CustomerDTO customerDTO = result.getResponseBody().customerDTO();
        String username = customerDTO.userName();
        assertThat(jwtUtil.isTokenValid(jwtToken, username));
        
        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.gender()).isEqualTo(gender);
        assertThat(customerDTO.name()).isEqualTo(name);
    }
}
