package org.example.services;

import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.model.Customer;
import org.example.model.PetStuff;
import org.example.model.Purchase;

import java.util.List;

public class PurchaseService implements Service<Purchase> {
    private final FileRepository<Purchase> purchaseRepository;
    private final AppHelper<Purchase> appHelperPurchase;
    private final AppHelper<Customer> appHelperCustomer;
    private final AppHelper<PetStuff> appHelperPetStuff;
    private final Input inputProvider;

    public PurchaseService(FileRepository<Purchase> purchaseRepository, AppHelper<Purchase> appHelperPurchase,
                           AppHelper<Customer> appHelperCustomer, AppHelper<PetStuff> appHelperPetStuff,
                           Input inputProvider) {
        this.purchaseRepository = purchaseRepository;
        this.appHelperPurchase = appHelperPurchase;
        this.appHelperCustomer = appHelperCustomer;
        this.appHelperPetStuff = appHelperPetStuff;
        this.inputProvider = inputProvider;
    }

    @Override
    public boolean add() {
        Customer customer = selectCustomer();
        if (customer == null) {
            System.out.println("Ошибка при добавлении покупки.");
            return false;
        }

        PetStuff petStuff = selectPetStuff();
        if (petStuff == null) {
            System.out.println("Ошибка при добавлении покупки.");
            return false;
        }

        boolean useDiscount = false;

        // Проверка, куплен ли товар со скидкой
        if (petStuff.isHasDiscount()) {
            System.out.print("Товар имеет скидку. Купить по скидке? (да/нет): ");
            String response = inputProvider.getInput().trim().toLowerCase();
            useDiscount = response.equals("да");
        }

        // Создание объекта Purchase с использованием параметра useDiscount
        Purchase purchase = new Purchase(customer, petStuff, useDiscount);
        purchaseRepository.save(purchase); // Сохранение покупки
        System.out.println("Покупка успешно добавлена.");
        return true;
    }

    @Override
    public void print() {
        List<Purchase> purchases = purchaseRepository.load(); // Загрузка покупок из репозитория
        if (purchases.isEmpty()) {
            System.out.println("Список покупок пуст.");
        } else {
            System.out.println("------ Список покупок ------");
            for (int i = 0; i < purchases.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, purchases.get(i));
            }
        }
    }

    @Override
    public List<Purchase> list() {
        return purchaseRepository.load();
    }

    @Override
    public boolean edit(Purchase purchase) {
        System.out.println("Редактирование покупки недоступно.");
        return false;
    }

    @Override
    public boolean remove(Purchase purchase) {
        List<Purchase> purchases = purchaseRepository.load(); // Загрузка последних покупок
        print();
        System.out.print("Введите номер покупки для удаления: ");
        int indexToRemove;
        try {
            indexToRemove = Integer.parseInt(inputProvider.getInput()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return false;
        }

        if (indexToRemove >= 0 && indexToRemove < purchases.size()) {
            purchases.remove(indexToRemove); // Удаление выбранной покупки
            purchaseRepository.save(purchases); // Сохранение обновлённого списка покупок
            System.out.println("Покупка успешно удалена.");
            return true;
        } else {
            System.out.println("Покупка не найдена.");
            return false;
        }
    }

    private Customer selectCustomer() {
        List<Customer> customers = appHelperCustomer.getRepository().load();
        appHelperCustomer.printList(customers);
        System.out.print("Введите номер покупателя для покупки: ");
        int customerIndex;
        try {
            customerIndex = Integer.parseInt(inputProvider.getInput()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return null;
        }

        if (customerIndex >= 0 && customerIndex < customers.size()) {
            return customers.get(customerIndex);
        }
        System.out.println("Некорректный выбор покупателя.");
        return null;
    }

    private PetStuff selectPetStuff() {
        List<PetStuff> petStuffs = appHelperPetStuff.getRepository().load();
        appHelperPetStuff.printList(petStuffs);
        System.out.print("Введите номер товара для покупки: ");
        int petStuffIndex;
        try {
            petStuffIndex = Integer.parseInt(inputProvider.getInput()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return null;
        }

        if (petStuffIndex >= 0 && petStuffIndex < petStuffs.size()) {
            return petStuffs.get(petStuffIndex);
        }
        System.out.println("Некорректный выбор товара.");
        return null;
    }
}
