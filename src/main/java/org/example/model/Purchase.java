package org.example.model;

import java.util.UUID;
import java.time.LocalDate;

public class Purchase {
    private UUID id;
    private Customer customer;
    private PetStuff petStuff;
    private LocalDate purchaseDate;
    private boolean useDiscount;

    public Purchase(Customer customer, PetStuff petStuff, boolean useDiscount) {
        this.id = UUID.randomUUID();
        this.customer = customer;
        this.petStuff = petStuff;
        this.purchaseDate = LocalDate.now();
        this.useDiscount = useDiscount;
    }

    public UUID getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PetStuff getPetStuff() {
        return petStuff;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isUseDiscount() {
        return useDiscount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Покупатель: ").append(customer);
        sb.append(", Товар: ").append(petStuff);

        if (useDiscount) {
            sb.append(" (приобретен по скидке за: ").append(petStuff.getDiscountedPrice()).append(")");
        } else {
            sb.append(" (приобретен по полной цене: ").append(petStuff.getPrice()).append(")");
        }

        sb.append(", Дата покупки: ").append(purchaseDate);
        return sb.toString();
    }
}
