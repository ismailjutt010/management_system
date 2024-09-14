package com.amigoscode.demo.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest ;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest =new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        //Then
        verify(customerRepository)
                .findAll();
    }

    @Test
    void getCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.getCustomerById(id);
        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                "Ismail",
                "ismail@gmail.com",
                25,

                Gender.MALE);
        // When
        underTest.insertCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existPersonWithEmail() {
        // Given
        String email = "ismail@gmail.com";
        // When
        underTest.existPersonWithEmail(email);
        //Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existPersonWithId() {
        // Given
        int id = 1;
        // When
        underTest.existPersonWithId(id);
        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.deleteCustomerById(id);
        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                "Ismail",
                "ismail@gmail.com",
                25,

                Gender.MALE);
        // When
        underTest.insertCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }
}