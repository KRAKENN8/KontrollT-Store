package org.example;

import org.example.interfaces.AppHelper;
import org.example.interfaces.Input;
import org.example.interfaces.repository.InMemoryRepository;
import org.example.services.CustomerService;
import org.example.services.PetStuffService;
import org.example.services.PurchaseService;
import org.example.model.Customer;
import org.example.model.PetStuff;
import org.example.model.Purchase;

import java.util.ArrayList;
import java.util.List;

public class NPTV23PetStore {
    public static void main(String[] args) {
        Input inputProvider = new AppHelper.ConsoleInput();

        InMemoryRepository<Customer> customerRepository = new InMemoryRepository<>();
        InMemoryRepository<PetStuff> petStuffRepository = new InMemoryRepository<>();
        InMemoryRepository<Purchase> purchaseRepository = new InMemoryRepository<>();

        AppHelper<Customer> appHelperCustomer = new AppHelperCustomer(customerRepository, inputProvider);
        AppHelper<PetStuff> appHelperPetStuff = new AppHelperPetStuff(petStuffRepository, inputProvider);
        AppHelper<Purchase> appHelperPurchase = new org.example.AppHelperPurchase(purchaseRepository, inputProvider, appHelperCustomer, appHelperPetStuff);

        List<Customer> customers = new ArrayList<>(customerRepository.load());
        List<PetStuff> petStuffs = new ArrayList<>(petStuffRepository.load());
        List<Purchase> purchases = new ArrayList<>(purchaseRepository.load());

        CustomerService customerService = new CustomerService(customers, appHelperCustomer, inputProvider);
        PetStuffService petStuffService = new PetStuffService(petStuffs, appHelperPetStuff, inputProvider);
        PurchaseService purchaseService = new PurchaseService(purchases, appHelperPurchase, appHelperCustomer, appHelperPetStuff, inputProvider);

        App app = new App(customerService, petStuffService, purchaseService, inputProvider);
        app.run();
    }
}
