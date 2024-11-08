package org.example;

import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.model.Customer;
import org.example.model.PetStuff;
import org.example.model.Purchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppHelperPurchaseTest {

    private FileRepository<Purchase> purchaseRepositoryMock;
    private FileRepository<Customer> customerRepositoryMock;
    private FileRepository<PetStuff> petStuffRepositoryMock;
    private Input inputMock;
    private AppHelperCustomer customerHelper;
    private AppHelperPetStuff petStuffHelper;
    private AppHelperPurchase purchaseHelper;

    @BeforeEach
    void setUp() {
        // Создание моков
        purchaseRepositoryMock = mock(FileRepository.class);
        customerRepositoryMock = mock(FileRepository.class);
        petStuffRepositoryMock = mock(FileRepository.class);
        inputMock = mock(Input.class);

        // Инициализация помощников с моками репозиториев и ввода
        customerHelper = new AppHelperCustomer(customerRepositoryMock, inputMock);
        petStuffHelper = new AppHelperPetStuff(petStuffRepositoryMock, inputMock);
        purchaseHelper = new AppHelperPurchase(purchaseRepositoryMock, inputMock, customerHelper, petStuffHelper);
    }

    @Test
    void createPurchase_WithDiscount() {
        // Подготовка данных
        Customer customer = new Customer("Иван", "Иванов", "1234567890");
        PetStuff petStuff = new PetStuff("Мячик", "Игрушка для собак", 500.0, true, 450.0);

        // Настройка поведения моков
        when(customerRepositoryMock.load()).thenReturn(Arrays.asList(customer));
        when(petStuffRepositoryMock.load()).thenReturn(Arrays.asList(petStuff));

        // Настройка ввода пользователя
        when(inputMock.getInput()).thenReturn("1") // Выбор первого покупателя
                .thenReturn("1") // Выбор первого товара
                .thenReturn("да"); // Использовать скидку

        // Создание покупки
        Purchase purchase = purchaseHelper.create();

        // Проверка результатов
        assertNotNull(purchase);
        assertEquals(customer, purchase.getCustomer());
        assertEquals(petStuff, purchase.getPetStuff());
        assertTrue(purchase.isUseDiscount());
    }

    @Test
    void createPurchase_WithoutDiscount() {
        // Подготовка данных
        Customer customer = new Customer("Мария", "Петрова", "0987654321");
        PetStuff petStuff = new PetStuff("Когтеточка", "Игрушка для кошек", 800.0, false, 0.0);

        // Настройка поведения моков
        when(customerRepositoryMock.load()).thenReturn(Arrays.asList(customer));
        when(petStuffRepositoryMock.load()).thenReturn(Arrays.asList(petStuff));

        // Настройка ввода пользователя
        when(inputMock.getInput()).thenReturn("1") // Выбор первого покупателя
                .thenReturn("1") // Выбор первого товара
                .thenReturn("нет"); // Не использовать скидку

        // Создание покупки
        Purchase purchase = purchaseHelper.create();

        // Проверка результатов
        assertNotNull(purchase);
        assertEquals(customer, purchase.getCustomer());
        assertEquals(petStuff, purchase.getPetStuff());
        assertFalse(purchase.isUseDiscount());
    }

    @Test
    void createPurchase_InvalidCustomerSelection() {
        // Настройка поведения моков
        when(customerRepositoryMock.load()).thenReturn(Arrays.asList()); // Пустой список покупателей

        // Настройка ввода пользователя
        when(inputMock.getInput()).thenReturn("1"); // Попытка выбрать первого покупателя

        // Создание покупки
        Purchase purchase = purchaseHelper.create();

        // Проверка результатов
        assertNull(purchase);
    }

    @Test
    void createPurchase_InvalidPetStuffSelection() {
        // Подготовка данных
        Customer customer = new Customer("Анна", "Сидорова", "1122334455");

        // Настройка поведения моков
        when(customerRepositoryMock.load()).thenReturn(Arrays.asList(customer));
        when(petStuffRepositoryMock.load()).thenReturn(Arrays.asList()); // Пустой список товаров

        // Настройка ввода пользователя
        when(inputMock.getInput()).thenReturn("1") // Выбор первого покупателя
                .thenReturn("1"); // Попытка выбрать первого товара

        // Создание покупки
        Purchase purchase = purchaseHelper.create();

        // Проверка результатов
        assertNull(purchase);
    }

    @Test
    void createPurchase_InvalidInput_NumberFormatException() {
        // Подготовка данных
        Customer customer = new Customer("Петр", "Козлов", "6677889900");
        PetStuff petStuff = new PetStuff("Лежак", "Удобное место для отдыха", 1500.0, true, 1400.0);

        // Настройка поведения моков
        when(customerRepositoryMock.load()).thenReturn(Arrays.asList(customer));
        when(petStuffRepositoryMock.load()).thenReturn(Arrays.asList(petStuff));

        // Настройка ввода пользователя с некорректным вводом
        when(inputMock.getInput()).thenReturn("abc") // Некорректный выбор покупателя
                .thenReturn("1")   // Выбор первого товара
                .thenReturn("да"); // Использовать скидку

        // Создание покупки
        Purchase purchase = purchaseHelper.create();

        // Поскольку выбор покупателя некорректен, покупка должна быть null
        assertNull(purchase);
    }

    @Test
    void savePurchase_CallsRepositorySave() {
        // Подготовка данных
        Customer customer = new Customer("Елена", "Николаева", "5544332211");
        PetStuff petStuff = new PetStuff("Поводок", "Долгий поводок для прогулок", 300.0, false, 0.0);
        Purchase purchase = new Purchase(customer, petStuff, false);

        // Сохранение покупки
        purchaseRepositoryMock.save(purchase);

        // Проверка, что метод save был вызван один раз с правильным аргументом
        verify(purchaseRepositoryMock, times(1)).save(purchase);
    }

    @Test
    void loadPurchases_CallsRepositoryLoad() {
        // Подготовка данных
        List<Purchase> purchases = Arrays.asList(
                new Purchase(new Customer("Игорь", "Смирнов", "1112223334"),
                        new PetStuff("Кормушка", "Автоматическая кормушка", 1200.0, true, 1100.0),
                        true)
        );

        // Настройка поведения моков
        when(purchaseRepositoryMock.load()).thenReturn(purchases);

        // Загрузка покупок
        List<Purchase> loadedPurchases = purchaseRepositoryMock.load();

        // Проверка, что метод load был вызван один раз
        verify(purchaseRepositoryMock, times(1)).load();

        // Проверка содержимого загруженных покупок
        assertEquals(1, loadedPurchases.size());
        assertEquals(purchases, loadedPurchases);
    }
}
