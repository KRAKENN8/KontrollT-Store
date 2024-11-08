package org.example.model;

import java.io.Serializable;

public class PetStuff implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private double price;
    private boolean hasDiscount;
    private double discountedPrice;

    public PetStuff() {}

    public PetStuff(String name, String description, double price, boolean hasDiscount, double discountedPrice) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.hasDiscount = hasDiscount;
        this.discountedPrice = discountedPrice;
    }

    // Геттеры и сеттеры

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Название: ").append(name);
        sb.append(", Описание: ").append(description);
        sb.append(", Цена: ").append(price);
        if (hasDiscount) {
            sb.append(" (Цена со скидкой: ").append(discountedPrice).append(")");
        }
        return sb.toString();
    }
}
