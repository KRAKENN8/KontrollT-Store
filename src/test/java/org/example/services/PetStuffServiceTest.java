package org.example.services;

import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.model.PetStuff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetStuffServiceTest {

    @Mock
    private FileRepository<PetStuff> petStuffRepository;

    @Mock
    private AppHelper<PetStuff> appHelperPetStuff;

    @Mock
    private Input inputProvider;

    @InjectMocks
    private PetStuffService petStuffService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add_ShouldAddPetStuffSuccessfully() {
        PetStuff petStuff = new PetStuff("Toy", "Cat toy", 15.99, true, 4.5);
        when(appHelperPetStuff.create()).thenReturn(petStuff);

        boolean result = petStuffService.add();

        assertTrue(result);
        verify(petStuffRepository, times(1)).save(petStuff);
    }

    @Test
    void add_ShouldReturnFalseWhenPetStuffCreationFails() {
        when(appHelperPetStuff.create()).thenReturn(null);

        boolean result = petStuffService.add();

        assertFalse(result);
        verify(petStuffRepository, never()).save(any(PetStuff.class));
    }

    @Test
    void print_ShouldPrintPetStuffList() {
        List<PetStuff> petStuffs = new ArrayList<>();
        petStuffs.add(new PetStuff("Toy", "Cat toy", 15.99, true, 4.5));
        when(petStuffRepository.load()).thenReturn(petStuffs);

        petStuffService.print();

        verify(appHelperPetStuff, times(1)).printList(petStuffs);
    }

    @Test
    void edit_ShouldEditPetStuffSuccessfully() {
        List<PetStuff> petStuffs = new ArrayList<>();
        PetStuff petStuff = new PetStuff("Toy", "Cat toy", 15.99, true, 4.5);
        petStuffs.add(petStuff);
        when(petStuffRepository.load()).thenReturn(petStuffs);
        when(inputProvider.getInput()).thenReturn("1");
        PetStuff updatedPetStuff = new PetStuff("Updated Toy", "Updated cat toy", 19.99, false, 3.5);
        when(appHelperPetStuff.create()).thenReturn(updatedPetStuff);

        boolean result = petStuffService.edit(petStuff);

        assertTrue(result);
        verify(petStuffRepository, times(1)).save(petStuffs);
        assertEquals(updatedPetStuff, petStuffs.get(0));
    }

    @Test
    void edit_ShouldReturnFalseWhenIndexIsInvalid() {
        List<PetStuff> petStuffs = new ArrayList<>();
        petStuffs.add(new PetStuff("Toy", "Cat toy", 15.99, true, 4.5));
        when(petStuffRepository.load()).thenReturn(petStuffs);
        when(inputProvider.getInput()).thenReturn("2"); // Invalid index

        boolean result = petStuffService.edit(petStuffs.get(0));

        assertFalse(result);
        verify(petStuffRepository, never()).save(petStuffs);
    }

    @Test
    void remove_ShouldRemovePetStuffSuccessfully() {
        List<PetStuff> petStuffs = new ArrayList<>();
        PetStuff petStuff = new PetStuff("Toy", "Cat toy", 15.99, true, 4.5);
        petStuffs.add(petStuff);
        when(petStuffRepository.load()).thenReturn(petStuffs);
        when(inputProvider.getInput()).thenReturn("1");

        boolean result = petStuffService.remove(petStuff);

        assertTrue(result);
        verify(petStuffRepository, times(1)).save(petStuffs);
        assertTrue(petStuffs.isEmpty());
    }

    @Test
    void remove_ShouldReturnFalseWhenIndexIsInvalid() {
        List<PetStuff> petStuffs = new ArrayList<>();
        petStuffs.add(new PetStuff("Toy", "Cat toy", 15.99, true, 4.5));
        when(petStuffRepository.load()).thenReturn(petStuffs);
        when(inputProvider.getInput()).thenReturn("2"); // Invalid index

        boolean result = petStuffService.remove(petStuffs.get(0));

        assertFalse(result);
        verify(petStuffRepository, never()).save(petStuffs);
    }
}
