package org.example.interfaces.framework.config;

import org.example.App;
import org.example.AppHelperCustomer;
import org.example.AppHelperPetStuff;
import org.example.AppHelperPurchase;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Service;
import org.example.interfaces.repository.FileStorage;
import org.example.model.Customer;
import org.example.model.PetStuff;
import org.example.model.Purchase;
import org.example.services.CustomerService;
import org.example.services.PetStuffService;
import org.example.services.PurchaseService;
import org.example.interfaces.Input;

import java.util.HashMap;
import java.util.Map;

public class JavaConfiguration implements Configuration {
    private final Map<String, Object> map = new HashMap<>();

    public JavaConfiguration() {
        init();
    }

    private void init() {
        // Input provider
        Input inputProvider = new AppHelper.ConsoleInput();

        // Storage (FileRepository)
        FileRepository<Customer> customerStorage = new FileStorage<>("customers.dat");
        FileRepository<PetStuff> petStuffStorage = new FileStorage<>("petstuff.dat");
        FileRepository<Purchase> purchaseStorage = new FileStorage<>("purchases.dat");

        // AppHelpers
        AppHelper<Customer> customerAppHelper = new AppHelperCustomer(customerStorage, inputProvider);
        AppHelper<PetStuff> petStuffAppHelper = new AppHelperPetStuff(petStuffStorage, inputProvider);
        AppHelper<Purchase> purchaseAppHelper = new AppHelperPurchase(purchaseStorage, inputProvider, customerAppHelper, petStuffAppHelper);

        // Services (use specific service classes)
        CustomerService customerService = new CustomerService(customerStorage, customerAppHelper, inputProvider);
        PetStuffService petStuffService = new PetStuffService(petStuffStorage, petStuffAppHelper, inputProvider);
        PurchaseService purchaseService = new PurchaseService(purchaseStorage, purchaseAppHelper, customerAppHelper, petStuffAppHelper, inputProvider);

        // App
        App app = new App(customerService, petStuffService, purchaseService, inputProvider);

        // Registering objects in the map
        map.put("inputProvider", inputProvider);
        map.put("customerStorage", customerStorage);
        map.put("petStuffStorage", petStuffStorage);
        map.put("purchaseStorage", purchaseStorage);
        map.put("customerAppHelper", customerAppHelper);
        map.put("petStuffAppHelper", petStuffAppHelper);
        map.put("purchaseAppHelper", purchaseAppHelper);
        map.put("customerService", customerService); // Store specific CustomerService here
        map.put("petStuffService", petStuffService);
        map.put("purchaseService", purchaseService);
        map.put("app", app);
    }

    @Override
    public Object getObject(String name) {
        return map.get(name);
    }
}
