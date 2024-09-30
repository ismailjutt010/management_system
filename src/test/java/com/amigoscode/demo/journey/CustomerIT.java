package com.amigoscode.demo.journey;

import com.amigoscode.demo.customer.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIT {

    private static final Random RANDOM = new Random();
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterACustomer() {

        //create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@testenvironment.com";
        int age = RANDOM.nextInt(1, 100);

        Gender gender = age % 2 == 0 ?Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                "password", age,
                gender
        );


        //send a post request
        String jwtToken = webTestClient.post()
                .uri("/api/v1/customers/addCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri("/api/v1/customers/getAllCustomers")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION , String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present



        int id = allCustomers.stream()
                .filter(c -> c.email().equals(email))
                .map(customer -> customer.id())
                .findFirst()
                .orElseThrow();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email);

        assertThat(allCustomers)
                .contains(expectedCustomer);

        // get customer by id
        webTestClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION , String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {

        //create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@testenvironment.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ?Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                "password", age,
                gender
        );

        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(
                name,
                email+".uk",
                "password", age,
                gender
        );

        //send a post request to create customer 1
        webTestClient.post()
                .uri("/api/v1/customers/addCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //send a post request to create customer 2
        String jwtToken = webTestClient.post()
                .uri("/api/v1/customers/addCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri("/api/v1/customers/getAllCustomers")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION , String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present

        int id = allCustomers.stream()
                .filter(c -> c.email().equals(email))
                .map(customer -> customer.id())
                .findFirst()
                .orElseThrow();

        //delete customer
        webTestClient.delete()
                .uri("api/v1/customers/deleteCustomerById/{id}", id)
                .header(HttpHeaders.AUTHORIZATION , String.format("Bearer %s",jwtToken))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();


        // get customer by id and make sure it is not found
        webTestClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION , String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {

        //create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@testenvironment.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ?Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, "password", age, gender
        );


        //send a post request
        String jwtToken = webTestClient.post()
                .uri("/api/v1/customers/addCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri("/api/v1/customers/getAllCustomers")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present

        int id = allCustomers.stream()
                .filter(c -> c.email().equals(email))
                .map(customer -> customer.id())
                .findFirst()
                .orElseThrow();

        //update customer
        String newName = "Ali";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName,null,null
        );


            webTestClient.put()
                .uri("api/v1/customers/updateCustomer/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        // get customer by id and make sure it is not found
        CustomerDTO updatedCustomer = webTestClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id, newName, email, gender, age,List.of("ROLE_USER"),email
        );

        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
    }
}
