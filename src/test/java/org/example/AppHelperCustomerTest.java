package org.example;

import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppHelperCustomerTest {

    @Mock
    private FileRepository<Customer> customerRepository;

    @Mock
    private Input inputProvider;

    @InjectMocks
    private AppHelperCustomer appHelperCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        when(inputProvider.getInput()).thenReturn("Иван", "Иванов", "123-456-7890");

        Customer customer = appHelperCustomer.create();

        assertEquals("Иван", customer.getName());
        assertEquals("Иванов", customer.getSurname());
        assertEquals("123-456-7890", customer.getPhone());
        verify(inputProvider, times(3)).getInput();
    }

    @Test
    void testPrintListEmpty() {
        appHelperCustomer.printList(List.of());

        verify(customerRepository, never()).load();
    }

    @Test
    void testPrintListWithCustomers() {
        Customer customer = new Customer("Анна", "Петрова", "987-654-3210");
        appHelperCustomer.printList(List.of(customer));

        verify(customerRepository, never()).load();
    }
}
