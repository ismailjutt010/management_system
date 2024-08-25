package com.amigoscode.demo.customer;

import com.amigoscode.demo.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
class CustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        // When
        boolean actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isTrue();
    }


    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        boolean actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        Integer id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // When

        boolean actual = underTest.existsCustomerById(id);

        //Then

        assertThat(actual).isTrue();
    }


    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        // Given
        Integer id = -1;
        // When
        boolean actual = underTest.existsCustomerById(id);
        //Then
        assertThat(actual).isFalse();
    }
}