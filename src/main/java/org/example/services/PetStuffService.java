package org.example.services;

import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.model.PetStuff;

import java.util.List;

public class PetStuffService implements Service<PetStuff> {
    private final FileRepository<PetStuff> petStuffRepository;
    private final AppHelper<PetStuff> appHelperPetStuff;
    private final Input inputProvider;

    public PetStuffService(FileRepository<PetStuff> petStuffRepository, AppHelper<PetStuff> appHelperPetStuff, Input inputProvider) {
        this.petStuffRepository = petStuffRepository;
        this.appHelperPetStuff = appHelperPetStuff;
        this.inputProvider = inputProvider;
    }

    @Override
    public boolean add() {
        PetStuff petStuff = appHelperPetStuff.create();
        if (petStuff != null) {
            petStuffRepository.save(petStuff);
            System.out.println("Товар успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении товара.");
        return false;
    }

    @Override
    public void print() {
        appHelperPetStuff.printList(petStuffRepository.load());
    }

    @Override
    public List<PetStuff> list() {
        return petStuffRepository.load();
    }

    @Override
    public boolean edit(PetStuff petStuff) {
        List<PetStuff> petStuffs = petStuffRepository.load();
        if (petStuffs.isEmpty()) {
            System.out.println("Список товаров пуст. Нечего редактировать.");
            return false;
        }

        print();
        System.out.print("Введите номер товара для редактирования: ");
        int indexToEdit;
        try {
            indexToEdit = Integer.parseInt(inputProvider.getInput()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return false;
        }

        if (indexToEdit >= 0 && indexToEdit < petStuffs.size()) {
            PetStuff updatedPetStuff = appHelperPetStuff.create();
            if (updatedPetStuff != null) {
                petStuffs.set(indexToEdit, updatedPetStuff);
                petStuffRepository.save(petStuffs);
                System.out.println("Товар успешно отредактирован.");
                return true;
            }
        } else {
            System.out.println("Некорректный выбор товара.");
        }
        return false;
    }

    @Override
    public boolean remove(PetStuff petStuff) {
        List<PetStuff> petStuffs = petStuffRepository.load();
        if (petStuffs.isEmpty()) {
            System.out.println("Список товаров пуст. Нечего удалять.");
            return false;
        }

        print();
        System.out.print("Введите номер товара для удаления: ");
        int indexToRemove;
        try {
            indexToRemove = Integer.parseInt(inputProvider.getInput()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return false;
        }

        if (indexToRemove >= 0 && indexToRemove < petStuffs.size()) {
            petStuffs.remove(indexToRemove);
            petStuffRepository.save(petStuffs);
            System.out.println("Товар успешно удален.");
            return true;
        } else {
            System.out.println("Некорректный выбор товара.");
        }
        return false;
    }
}
