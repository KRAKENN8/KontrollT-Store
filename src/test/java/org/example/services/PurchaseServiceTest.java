package org.example.services;

import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.model.Customer;
import org.example.model.PetStuff;
import org.example.model.Purchase;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

public class PurchaseServiceTest {

    private PurchaseService purchaseService;
    private FileRepository<Purchase> purchaseRepository;
    private AppHelper<Purchase> appHelperPurchase;
    private AppHelper<Customer> appHelperCustomer;
    private AppHelper<PetStuff> appHelperPetStuff;
    private Input inputProvider;

    // Mocked repositories for Customer and PetStuff
    private FileRepository<Customer> customerRepository;
    private FileRepository<PetStuff> petStuffRepository;

    @BeforeEach
    public void setUp() {
        purchaseRepository = Mockito.mock(FileRepository.class);
        appHelperPurchase = Mockito.mock(AppHelper.class);
        appHelperCustomer = Mockito.mock(AppHelper.class);
        appHelperPetStuff = Mockito.mock(AppHelper.class);
        inputProvider = Mockito.mock(Input.class);

        // Create mocks for the repositories
        customerRepository = Mockito.mock(FileRepository.class);
        petStuffRepository = Mockito.mock(FileRepository.class);

        // Set up the AppHelpers to return the mocked repositories
        Mockito.when(appHelperCustomer.getRepository()).thenReturn(customerRepository);
        Mockito.when(appHelperPetStuff.getRepository()).thenReturn(petStuffRepository);

        purchaseService = new PurchaseService(
                purchaseRepository,
                appHelperPurchase,
                appHelperCustomer,
                appHelperPetStuff,
                inputProvider
        );
    }

    @Test
    public void testAddPurchaseSuccessfully() {
        // Mock customers
        List<Customer> customers = Arrays.asList(
                new Customer("John", "Doe", "1234567890"),
                new Customer("Jane", "Smith", "0987654321")
        );
        Mockito.when(customerRepository.load()).thenReturn(customers);
        Mockito.doNothing().when(appHelperCustomer).printList(customers);

        // Mock petStuffs
        List<PetStuff> petStuffs = Arrays.asList(
                new PetStuff("Dog Toy", "Toys", 10.0, true, 0.1),
                new PetStuff("Cat Food", "Food", 5.0, false, 0.0)
        );
        Mockito.when(petStuffRepository.load()).thenReturn(petStuffs);
        Mockito.doNothing().when(appHelperPetStuff).printList(petStuffs);

        // Mock user input
        Mockito.when(inputProvider.getInput())
                .thenReturn("1") // Customer index
                .thenReturn("1") // PetStuff index
                .thenReturn("да"); // Use discount

        Mockito.doNothing().when(purchaseRepository).save(Mockito.any(Purchase.class));

        // Execute
        boolean result = purchaseService.add();

        // Verify
        Mockito.verify(appHelperCustomer).printList(customers);
        Mockito.verify(appHelperPetStuff).printList(petStuffs);
        Mockito.verify(purchaseRepository).save(Mockito.any(Purchase.class));

        Assertions.assertTrue(result);
    }

    @Test
    public void testAddPurchaseWithoutDiscount() {
        // Mock customers
        List<Customer> customers = Arrays.asList(
                new Customer("John", "Doe", "1234567890"),
                new Customer("Jane", "Smith", "0987654321")
        );
        Mockito.when(customerRepository.load()).thenReturn(customers);
        Mockito.doNothing().when(appHelperCustomer).printList(customers);

        // Mock petStuffs without discounts
        List<PetStuff> petStuffs = Arrays.asList(
                new PetStuff("Dog Toy", "Toys", 10.0, false, 0.0),
                new PetStuff("Cat Food", "Food", 5.0, false, 0.0)
        );
        Mockito.when(petStuffRepository.load()).thenReturn(petStuffs);
        Mockito.doNothing().when(appHelperPetStuff).printList(petStuffs);

        // Mock user input
        Mockito.when(inputProvider.getInput())
                .thenReturn("1") // Customer index
                .thenReturn("1"); // PetStuff index

        Mockito.doNothing().when(purchaseRepository).save(Mockito.any(Purchase.class));

        // Execute
        boolean result = purchaseService.add();

        // Verify
        Mockito.verify(appHelperCustomer).printList(customers);
        Mockito.verify(appHelperPetStuff).printList(petStuffs);
        Mockito.verify(purchaseRepository).save(Mockito.any(Purchase.class));

        Assertions.assertTrue(result);
    }

    @Test
    public void testAddPurchaseInvalidCustomerSelection() {
        // Mock customers
        List<Customer> customers = Arrays.asList(
                new Customer("John", "Doe", "1234567890"),
                new Customer("Jane", "Smith", "0987654321")
        );
        Mockito.when(customerRepository.load()).thenReturn(customers);
        Mockito.doNothing().when(appHelperCustomer).printList(customers);

        // Mock invalid input
        Mockito.when(inputProvider.getInput())
                .thenReturn("5"); // Invalid customer index

        // Execute
        boolean result = purchaseService.add();

        // Verify
        Assertions.assertFalse(result);
        Mockito.verifyNoInteractions(appHelperPetStuff);
        Mockito.verify(purchaseRepository, Mockito.never()).save(Mockito.any(Purchase.class));
    }

    @Test
    public void testRemovePurchaseSuccessfully() {
        // Mock purchases
        Purchase purchase1 = new Purchase(
                new Customer("John", "Doe", "1234567890"),
                new PetStuff("Dog Toy", "Toys", 10.0, true, 0.1),
                false
        );
        Purchase purchase2 = new Purchase(
                new Customer("Jane", "Smith", "0987654321"),
                new PetStuff("Cat Food", "Food", 5.0, false, 0.0),
                false
        );
        List<Purchase> purchases = new ArrayList<>(Arrays.asList(purchase1, purchase2));
        Mockito.when(purchaseRepository.load()).thenReturn(purchases);

        // Mock input
        Mockito.when(inputProvider.getInput()).thenReturn("1"); // Remove first purchase

        Mockito.doNothing().when(purchaseRepository).save(Mockito.anyList());

        // Execute
        boolean result = purchaseService.remove(null);

        // Verify
        Assertions.assertTrue(result);
        Mockito.verify(purchaseRepository).save(Mockito.anyList());

        // Capture saved list
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Purchase>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(purchaseRepository).save(captor.capture());
        List<Purchase> updatedPurchases = captor.getValue();

        Assertions.assertEquals(1, updatedPurchases.size());
        Assertions.assertFalse(updatedPurchases.contains(purchase1));
    }

    @Test
    public void testRemovePurchaseInvalidIndex() {
        // Mock purchases
        Purchase purchase1 = new Purchase(
                new Customer("John", "Doe", "1234567890"),
                new PetStuff("Dog Toy", "Toys", 10.0, true, 0.1),
                false
        );
        List<Purchase> purchases = new ArrayList<>(Collections.singletonList(purchase1));
        Mockito.when(purchaseRepository.load()).thenReturn(purchases);

        // Mock invalid input
        Mockito.when(inputProvider.getInput()).thenReturn("5"); // Invalid index

        // Execute
        boolean result = purchaseService.remove(null);

        // Verify
        Assertions.assertFalse(result);
        Mockito.verify(purchaseRepository, Mockito.never()).save(Mockito.anyList());
    }

    @Test
    public void testEditNotAvailable() {
        Purchase purchase = new Purchase(
                new Customer("John", "Doe", "1234567890"),
                new PetStuff("Dog Toy", "Toys", 10.0, true, 0.1),
                false
        );
        boolean result = purchaseService.edit(purchase);
        Assertions.assertFalse(result);
    }

    @Test
    public void testListPurchases() {
        // Mock purchases
        Purchase purchase1 = new Purchase(
                new Customer("John", "Doe", "1234567890"),
                new PetStuff("Dog Toy", "Toys", 10.0, true, 0.1),
                false
        );
        List<Purchase> purchases = Collections.singletonList(purchase1);
        Mockito.when(purchaseRepository.load()).thenReturn(purchases);

        // Execute
        List<Purchase> result = purchaseService.list();

        // Verify
        Assertions.assertEquals(purchases, result);
    }
}
