package org.example;

import org.example.interfaces.AppHelper;
import org.example.interfaces.Input;
import org.example.interfaces.FileRepository;
import org.example.model.PetStuff;

import java.util.List;

public class AppHelperPetStuff implements AppHelper<PetStuff> {
    private final FileRepository<PetStuff> petStuffRepository;
    private final Input inputProvider;

    public AppHelperPetStuff(FileRepository<PetStuff> petStuffRepository, Input inputProvider) {
        this.petStuffRepository = petStuffRepository;
        this.inputProvider = inputProvider;
    }

    @Override
    public PetStuff create() {
        PetStuff petStuff = new PetStuff();
        System.out.print("Введите название товара: ");
        petStuff.setName(inputProvider.getInput());
        System.out.print("Введите описание товара: ");
        petStuff.setDescription(inputProvider.getInput());

        double price;
        while (true) {
            try {
                System.out.print("Введите цену товара: ");
                price = Double.parseDouble(inputProvider.getInput());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите корректное числовое значение для цены.");
            }
        }
        petStuff.setPrice(price);

        System.out.print("Есть ли скидка на товар? (да/нет): ");
        String discountInput = inputProvider.getInput();
        if (discountInput.equalsIgnoreCase("да")) {
            petStuff.setHasDiscount(true);
            double discountedPrice;
            while (true) {
                try {
                    System.out.print("Введите цену со скидкой: ");
                    discountedPrice = Double.parseDouble(inputProvider.getInput());
                    if (discountedPrice >= price) {
                        System.out.println("Цена со скидкой должна быть меньше исходной цены.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Пожалуйста, введите корректное числовое значение для цены со скидкой.");
                }
            }
            petStuff.setDiscountedPrice(discountedPrice);
        } else {
            petStuff.setHasDiscount(false);
        }

        return petStuff;
    }

    @Override
    public void printList(List<PetStuff> petStuffs) {
        if (petStuffs.isEmpty()) {
            System.out.println("Список товаров пуст.");
            return;
        }
        System.out.println("------ Список товаров ------");
        for (int i = 0; i < petStuffs.size(); i++) {
            PetStuff petStuff = petStuffs.get(i);
            System.out.printf("%d. Название: %s, Описание: %s, Цена: %.2f%s%n",
                    i + 1,
                    petStuff.getName(),
                    petStuff.getDescription(),
                    petStuff.getPrice(),
                    petStuff.isHasDiscount() ? ", Цена со скидкой: " + petStuff.getDiscountedPrice() : ""
            );
        }
    }

    @Override
    public FileRepository<PetStuff> getRepository() {
        return petStuffRepository;
    }
}
