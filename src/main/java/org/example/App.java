package org.example;

import org.example.interfaces.Input;
import org.example.services.CustomerService;
import org.example.services.PetStuffService;
import org.example.services.PurchaseService;
import org.example.model.Customer;
import org.example.model.PetStuff;
import org.example.model.Purchase;

public class App {
    private final CustomerService customerService;
    private final PetStuffService petStuffService;
    private final PurchaseService purchaseService;
    private final Input inputProvider;

    public App(CustomerService customerService, PetStuffService petStuffService, PurchaseService purchaseService, Input inputProvider) {
        this.customerService = customerService;
        this.petStuffService = petStuffService;
        this.purchaseService = purchaseService;
        this.inputProvider = inputProvider;
    }

    public void run() {
        System.out.println("------ Магазин товаров для домашних животных ------");
        boolean repeat = true;
        do {
            System.out.println("\nМеню:");
            System.out.println("0. Выйти из программы");
            System.out.println("1. Добавить товар");
            System.out.println("2. Список товаров");
            System.out.println("3. Редактировать товар");
            System.out.println("4. Удалить товар");
            System.out.println("5. Добавить покупателя");
            System.out.println("6. Список покупателей");
            System.out.println("7. Редактировать покупателя");
            System.out.println("8. Удалить покупателя");
            System.out.println("9. Купить товар");
            System.out.println("10. История покупок");
            System.out.print("Введите номер задачи: ");

            int task;
            try {
                task = Integer.parseInt(inputProvider.getInput());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите число от 0 до 10.");
                continue;
            }

            switch (task) {
                case 0:
                    repeat = false;
                    break;
                case 1:
                    petStuffService.add();
                    break;
                case 2:
                    petStuffService.print();
                    break;
                case 3:
                    petStuffService.edit(null);  // Вызов метода edit, который сам покажет список
                    break;
                case 4:
                    petStuffService.remove(null);
                    break;
                case 5:
                    customerService.add();
                    break;
                case 6:
                    customerService.print();
                    break;
                case 7:
                    customerService.edit(null);
                    break;
                case 8:
                    customerService.remove(null);
                    break;
                case 9:
                    purchaseService.add();
                    break;
                case 10:
                    purchaseService.print();
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, введите число от 0 до 10.");
            }
        } while (repeat);

        System.out.println("До свидания!");
    }
}
