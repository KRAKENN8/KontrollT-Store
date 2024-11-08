package org.example.services;

import org.example.interfaces.AppHelper;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.model.PetStuff;

import java.util.List;

public class PetStuffService implements Service<PetStuff> {
    private final List<PetStuff> petStuffs;
    private final AppHelper<PetStuff> appHelperPetStuff;
    private final Input inputProvider;

    public PetStuffService(List<PetStuff> petStuffs, AppHelper<PetStuff> appHelperPetStuff, Input inputProvider) {
        this.petStuffs = petStuffs;
        this.appHelperPetStuff = appHelperPetStuff;
        this.inputProvider = inputProvider;
    }

    @Override
    public boolean add() {
        PetStuff petStuff = appHelperPetStuff.create();
        if (petStuff != null) {
            petStuffs.add(petStuff);
            appHelperPetStuff.getRepository().save(petStuffs);
            System.out.println("Товар успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении товара.");
        return false;
    }

    @Override
    public void print() {
        appHelperPetStuff.printList(petStuffs);
    }

    @Override
    public List<PetStuff> list() {
        return petStuffs;
    }

    @Override
    public boolean edit(PetStuff petStuff) {
        if (petStuffs.isEmpty()) {
            System.out.println("Список товаров пуст. Нечего редактировать.");
            return false;
        }

        print();
        System.out.print("Введите номер товара для редактирования: ");
        int indexToEdit = Integer.parseInt(inputProvider.getInput()) - 1;

        if (indexToEdit >= 0 && indexToEdit < petStuffs.size()) {
            PetStuff updatedPetStuff = appHelperPetStuff.create();
            petStuffs.set(indexToEdit, updatedPetStuff);
            appHelperPetStuff.getRepository().save(petStuffs);
            System.out.println("Товар успешно отредактирован.");
            return true;
        } else {
            System.out.println("Некорректный выбор товара.");
            return false;
        }
    }

    @Override
    public boolean remove(PetStuff petStuff) {
        if (petStuffs.isEmpty()) {
            System.out.println("Список товаров пуст. Нечего удалять.");
            return false;
        }

        print();
        System.out.print("Введите номер товара для удаления: ");
        int indexToRemove = Integer.parseInt(inputProvider.getInput()) - 1;

        if (indexToRemove >= 0 && indexToRemove < petStuffs.size()) {
            petStuffs.remove(indexToRemove);
            appHelperPetStuff.getRepository().save(petStuffs);
            System.out.println("Товар успешно удален.");
            return true;
        } else {
            System.out.println("Некорректный выбор товара.");
            return false;
        }
    }
}
