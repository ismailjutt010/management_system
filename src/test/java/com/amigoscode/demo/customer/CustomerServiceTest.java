package com.amigoscode.demo.customer;

import com.amigoscode.demo.exceptions.DuplicateResourceException;
import com.amigoscode.demo.exceptions.RequestValidationException;
import com.amigoscode.demo.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }


    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();
        //Then
        verify(customerDAO).getAllCustomers();
    }

    @Test
    void getCustomerById() {
        // Given
        int id = 10;
        Customer c =new Customer(
                id,
                "alex",
                "alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(c));
        // When
        Customer actual = underTest.getCustomerById(10);
        //Then
        assertThat(actual).isEqualTo(c);
    }

    @Test
    void willThroughWhenGetCustomerByIdReturnEmptyOptional() {
        // Given
        int id = 10;

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(
                ()->underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "Customer with id [%s] not found".formatted(id)
                );
    }

    @Test
    void addCustomer() {
        // Given
        String email = "alex@gmail.com";
        when(customerDAO.existPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                 "Alex",
                 email,
                19,
                Gender.MALE
        );
        // When
        underTest.addCustomer(registrationRequest);
        //Then

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(registrationRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(registrationRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(registrationRequest.email());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "alex@gmail.com";
        when(customerDAO.existPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                "Alex",
                email,
                19,
                Gender.MALE
        );
        // When
        assertThatThrownBy(()->underTest.addCustomer(registrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email Already Taken");
        //Then

                ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO , never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        // Given
        Integer id = 10;
        when(customerDAO.existPersonWithId(id)).thenReturn(true);
        // When
        underTest.deleteCustomer(id);

        //Then

        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenDeleteCustomerNotExist() {
        // Given
        Integer id = 10;
        when(customerDAO.existPersonWithId(id)).thenReturn(false);
        // When
        assertThatThrownBy(()->underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

        //Then

        verify(customerDAO , never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomerAllProperties() {
        // Given
        int id = 10;
        Customer customer =new Customer(
                id,
                "alex",
                "alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer ));

        // When
        String newEmail = "Alexandra@ismail.com";
        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(
                "Alexandra",
                newEmail
                , 23
        );
        when(customerDAO.existPersonWithEmail(newEmail)).thenReturn(false);
        underTest.updateCustomer(id, updateReq);
        //Then

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO, times(3)).updateCustomer(argumentCaptor.capture());


        Customer capturedCustomer = argumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(updateReq.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateReq.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateReq.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 10;
        Customer customer =new Customer(
                id,
                "alex",
                "alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer ));

        // When

        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(
                "Alexandra",
                null
                , null
        );

        underTest.updateCustomer(id, updateReq);
        //Then

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO, times(1)).updateCustomer(argumentCaptor.capture());


        Customer capturedCustomer = argumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(updateReq.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 10;
        Customer customer =new Customer(
                id,
                "alex",
                "alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer ));

        // When

        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(
                null,
                null,
                23
        );

        underTest.updateCustomer(id, updateReq);
        //Then

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO, times(1)).updateCustomer(argumentCaptor.capture());


        Customer capturedCustomer = argumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateReq.age());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 10;
        Customer customer =new Customer(
                id,
                "alex",
                "alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer ));

        // When
        String newEmail = "Alexandra@ismail.com";
        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(
                null,
                newEmail,
                null
        );
        when(customerDAO.existPersonWithEmail(newEmail)).thenReturn(false);
        underTest.updateCustomer(id, updateReq);
        //Then

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(argumentCaptor.capture());


        Customer capturedCustomer = argumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateReq.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }


    @Test
    void willThorwWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        // Given
        int id = 10;
        Customer customer =new Customer(
                id,
                "alex",
                "alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer ));

        // When
        String newEmail = "Alexandra@ismail.com";
        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(
                null,
                newEmail,
                null
        );
        when(customerDAO.existPersonWithEmail(newEmail)).thenReturn(true);

        assertThatThrownBy(()->underTest.updateCustomer(id,updateReq))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email Already Taken");
        //Then
        verify(customerDAO , never()).updateCustomer(any());
        }


    @Test
    void willThorwWhenCustomerUpdateHasNoChanges() {
        // Given
        int id = 10;
        Customer customer =new Customer(
                id,
                "alex",
                "alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer ));

        // When
        String newEmail = "Alexandra@ismail.com";
        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        assertThatThrownBy(()->underTest.updateCustomer(id, updateReq))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        verify(customerDAO , never()).updateCustomer(any());
    }
}