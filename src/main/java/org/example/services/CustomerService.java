package org.example.services;

import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.model.Customer;

import java.util.List;

public class CustomerService implements Service<Customer> {
    private final FileRepository<Customer> customerRepository;
    private final AppHelper<Customer> appHelperCustomer;
    private final Input inputProvider;

    public CustomerService(FileRepository<Customer> customerRepository, AppHelper<Customer> appHelperCustomer, Input inputProvider) {
        this.customerRepository = customerRepository;
        this.appHelperCustomer = appHelperCustomer;
        this.inputProvider = inputProvider;
    }

    @Override
    public boolean add() {
        Customer customer = appHelperCustomer.create();
        if (customer != null) {
            customerRepository.save(customer);
            System.out.println("Покупатель успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении покупателя.");
        return false;
    }

    @Override
    public void print() {
        appHelperCustomer.printList(customerRepository.load());
    }

    @Override
    public List<Customer> list() {
        return customerRepository.load();
    }

    @Override
    public boolean edit(Customer customer) {
        List<Customer> customers = customerRepository.load();
        if (customers.isEmpty()) {
            System.out.println("Список покупателей пуст. Нечего редактировать.");
            return false;
        }

        print();
        System.out.print("Введите номер покупателя для редактирования: ");
        int indexToEdit;
        try {
            indexToEdit = Integer.parseInt(inputProvider.getInput()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return false;
        }

        if (indexToEdit >= 0 && indexToEdit < customers.size()) {
            Customer updatedCustomer = appHelperCustomer.create();
            if (updatedCustomer != null) {
                // Сохранить тот же UUID
                updatedCustomer = new Customer(updatedCustomer.getName(), updatedCustomer.getSurname(), updatedCustomer.getPhone());
                customers.set(indexToEdit, updatedCustomer);
                customerRepository.save(customers);
                System.out.println("Покупатель успешно отредактирован.");
                return true;
            }
        } else {
            System.out.println("Некорректный выбор покупателя.");
        }
        return false;
    }

    @Override
    public boolean remove(Customer customer) {
        List<Customer> customers = customerRepository.load();
        if (customers.isEmpty()) {
            System.out.println("Список покупателей пуст. Нечего удалять.");
            return false;
        }

        print();
        System.out.print("Введите номер покупателя для удаления: ");
        int indexToRemove;
        try {
            indexToRemove = Integer.parseInt(inputProvider.getInput()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return false;
        }

        if (indexToRemove >= 0 && indexToRemove < customers.size()) {
            customers.remove(indexToRemove);
            customerRepository.save(customers);
            System.out.println("Покупатель успешно удален.");
            return true;
        } else {
            System.out.println("Некорректный выбор покупателя.");
        }
        return false;
    }
}
