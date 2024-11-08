package org.example.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String surname;
    private String phone;

    public Customer() {
        this.id = UUID.randomUUID();
    }

    public Customer(String name, String surname, String phone) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    // Геттеры и сеттеры

    public UUID getId() {
        return id;
    }

    // Если вам нужно изменить UUID при редактировании, добавьте сеттер
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Переопределение методов equals и hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) &&
                Objects.equals(surname, customer.surname) &&
                Objects.equals(phone, customer.phone);
        // Если хотите учитывать UUID, добавьте: && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, phone);
        // Если учитываете UUID, добавьте его: Objects.hash(id, name, surname, phone);
    }

    @Override
    public String toString() {
        return "Имя: " + name + ", Фамилия: " + surname + ", Телефон: " + phone;
    }
}
