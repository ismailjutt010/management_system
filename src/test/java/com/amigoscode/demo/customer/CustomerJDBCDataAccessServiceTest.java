package com.amigoscode.demo.customer;

import com.amigoscode.demo.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private  CustomerJDBCDataAccessService underTest;
    private final  CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void getAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() +"-"+UUID.randomUUID(),
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        // When

        List<Customer> allCustomers = underTest.getAllCustomers();

        //Then

        assertThat(allCustomers).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Integer id = underTest.getAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // When

        Optional<Customer> actual = underTest.getCustomerById(id);

        //Then

        assertThat(actual).isPresent().hasValueSatisfying( c -> {
           assertThat(c.getId()).isEqualTo(id);
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById(){
        int id = -1;

        var actual = underTest.getCustomerById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {

        
        // Given

        // When

        //Then
    }

    @Test
    void existPersonWithEmail() {
        // Given

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        // When

        boolean actual = underTest.existPersonWithEmail(email);

        //Then

        assertThat(actual).isTrue();
    }


    @Test
    void existPersonWithEmailReturnFalseWhenDoesNotExist() {
        // Given

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When

        boolean actual = underTest.existPersonWithEmail(email);

        //Then

        assertThat(actual).isFalse();
    }

    @Test
    void existPersonWithId() {
        // Given

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        int id = underTest.getAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(customer1 -> customer1.getId())
                .findFirst()
                .orElseThrow();

        // When

        var actual =  underTest.existPersonWithId(id);

        //Then

        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithIdWillReturnFalseWhenIdNotPresent() {
        // Given
        int id = -1 ;
        // When
        boolean actual = underTest.existPersonWithId(id);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        int id = underTest.getAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(customer1 -> customer1.getId())
                .findFirst()
                .orElseThrow();

        // When

        underTest.deleteCustomerById(id);
        //Then

        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.getAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(customer1 -> customer1.getId())
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When

        Customer update =  new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then

        Optional<Customer> actual = underTest.getCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c ->{
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(newName);
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }


    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.getAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(customer1 -> customer1.getId())
                .findFirst()
                .orElseThrow();

        var newEmail =FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When

        Customer update =  new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Then

        Optional<Customer> actual = underTest.getCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c ->{
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(newEmail);
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.getAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(customer1 -> customer1.getId())
                .findFirst()
                .orElseThrow();

        var newAge = 110;

        // When

        Customer update =  new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //Then

        Optional<Customer> actual = underTest.getCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c ->{
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(newAge);
                }
        );
    }


    @Test
    void updateAllPropertiesCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.getAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(customer1 -> customer1.getId())
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When

        Customer update =  new Customer();
        update.setId(id);
        update.setName("food");
        update.setEmail("food@gmail.com");
        update.setAge(89);

        underTest.updateCustomer(update);

        //Then

        Optional<Customer> actual = underTest.getCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated ->{
           assertThat(updated.getId()).isEqualTo(id);
           assertThat(updated.getGender()).isEqualTo(Gender.MALE);
           assertThat(updated.getName()).isEqualTo("food");
           assertThat(updated.getEmail()).isEqualTo("food@gmail.com");
           assertThat(updated.getAge()).isEqualTo(89);
        });
    }
}