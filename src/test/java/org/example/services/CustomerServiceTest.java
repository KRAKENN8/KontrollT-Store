package org.example.services;

import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private FileRepository<Customer> customerRepository;

    @Mock
    private AppHelper<Customer> appHelperCustomer;

    @Mock
    private Input inputProvider;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add_ShouldAddCustomerSuccessfully() {
        Customer customer = new Customer("John", "Doe", "johndoe@example.com");
        when(appHelperCustomer.create()).thenReturn(customer);

        boolean result = customerService.add();

        assertTrue(result);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void add_ShouldReturnFalseWhenCustomerCreationFails() {
        when(appHelperCustomer.create()).thenReturn(null);

        boolean result = customerService.add();

        assertFalse(result);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void print_ShouldPrintCustomerList() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John", "Doe", "johndoe@example.com"));
        when(customerRepository.load()).thenReturn(customers);

        customerService.print();

        verify(appHelperCustomer, times(1)).printList(customers);
    }

    @Test
    void edit_ShouldEditCustomerSuccessfully() {
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer("John", "Doe", "johndoe@example.com");
        customers.add(customer);
        when(customerRepository.load()).thenReturn(customers);
        when(inputProvider.getInput()).thenReturn("1");
        Customer updatedCustomer = new Customer("Jane", "Doe", "janedoe@example.com");
        when(appHelperCustomer.create()).thenReturn(updatedCustomer);

        boolean result = customerService.edit(customer);

        assertTrue(result);
        verify(customerRepository, times(1)).save(customers);
        assertEquals(updatedCustomer, customers.get(0));
    }

    @Test
    void edit_ShouldReturnFalseWhenIndexIsInvalid() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John", "Doe", "johndoe@example.com"));
        when(customerRepository.load()).thenReturn(customers);
        when(inputProvider.getInput()).thenReturn("2"); // Invalid index

        boolean result = customerService.edit(customers.get(0));

        assertFalse(result);
        verify(customerRepository, never()).save(customers);
    }

    @Test
    void remove_ShouldRemoveCustomerSuccessfully() {
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer("John", "Doe", "johndoe@example.com");
        customers.add(customer);
        when(customerRepository.load()).thenReturn(customers);
        when(inputProvider.getInput()).thenReturn("1");

        boolean result = customerService.remove(customer);

        assertTrue(result);
        verify(customerRepository, times(1)).save(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void remove_ShouldReturnFalseWhenIndexIsInvalid() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John", "Doe", "johndoe@example.com"));
        when(customerRepository.load()).thenReturn(customers);
        when(inputProvider.getInput()).thenReturn("2"); // Invalid index

        boolean result = customerService.remove(customers.get(0));

        assertFalse(result);
        verify(customerRepository, never()).save(customers);
    }
}
