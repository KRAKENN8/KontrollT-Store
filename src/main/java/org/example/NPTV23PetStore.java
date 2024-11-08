package org.example;

import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.interfaces.repository.FileStorage;
import org.example.services.CustomerService;
import org.example.services.PetStuffService;
import org.example.services.PurchaseService;
import org.example.model.Customer;
import org.example.model.PetStuff;
import org.example.model.Purchase;

public class NPTV23PetStore {
    public static void main(String[] args) {
        Input inputProvider = new AppHelper.ConsoleInput();

        FileRepository<Customer> customerRepository = new FileStorage<>("Customer") {};
        FileRepository<PetStuff> petStuffRepository = new FileStorage<>("PetStuff") {};
        FileRepository<Purchase> purchaseRepository = new FileStorage<>("Purchase") {};

        AppHelper<Customer> appHelperCustomer = new AppHelperCustomer(customerRepository, inputProvider);
        AppHelper<PetStuff> appHelperPetStuff = new AppHelperPetStuff(petStuffRepository, inputProvider);
        AppHelper<Purchase> appHelperPurchase = new AppHelperPurchase(purchaseRepository, inputProvider, appHelperCustomer, appHelperPetStuff);

        CustomerService customerService = new CustomerService(customerRepository, appHelperCustomer, inputProvider);
        PetStuffService petStuffService = new PetStuffService(petStuffRepository, appHelperPetStuff, inputProvider);
        PurchaseService purchaseService = new PurchaseService(purchaseRepository, appHelperPurchase, appHelperCustomer, appHelperPetStuff, inputProvider);

        App app = new App(customerService, petStuffService, purchaseService, inputProvider);
        app.run();
    }
}
