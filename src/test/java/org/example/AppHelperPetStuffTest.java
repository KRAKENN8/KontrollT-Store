package org.example;

import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.model.PetStuff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AppHelperPetStuffTest {

    @Mock
    private FileRepository<PetStuff> petStuffRepository;

    @Mock
    private Input inputProvider;

    @InjectMocks
    private AppHelperPetStuff appHelperPetStuff;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePetStuffWithDiscount() {
        when(inputProvider.getInput()).thenReturn("Корм для кошек", "Премиум корм", "100.0", "да", "80.0");

        PetStuff petStuff = appHelperPetStuff.create();

        assertEquals("Корм для кошек", petStuff.getName());
        assertEquals("Премиум корм", petStuff.getDescription());
        assertEquals(100.0, petStuff.getPrice());
        assertTrue(petStuff.isHasDiscount());
        assertEquals(80.0, petStuff.getDiscountedPrice());
        verify(inputProvider, times(5)).getInput();
    }

    @Test
    void testCreatePetStuffWithoutDiscount() {
        when(inputProvider.getInput()).thenReturn("Корм для собак", "Органический корм", "120.0", "нет");

        PetStuff petStuff = appHelperPetStuff.create();

        assertEquals("Корм для собак", petStuff.getName());
        assertEquals("Органический корм", petStuff.getDescription());
        assertEquals(120.0, petStuff.getPrice());
        assertTrue(!petStuff.isHasDiscount());
        verify(inputProvider, times(4)).getInput();
    }

    @Test
    void testPrintListEmpty() {
        appHelperPetStuff.printList(List.of());

        verify(petStuffRepository, never()).load();
    }

    @Test
    void testPrintListWithItems() {
        PetStuff petStuff = new PetStuff("Игрушка для кошек", "Мягкая игрушка", 50.0, false, 0.0);
        appHelperPetStuff.printList(List.of(petStuff));

        verify(petStuffRepository, never()).load();
    }
}
