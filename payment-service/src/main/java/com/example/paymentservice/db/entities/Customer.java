package com.example.paymentservice.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int amountAvailable;
    private int amountReserved;
    private int amountSpent;

    public void reserve(int amount) {
        amountAvailable -= amount;
        amountReserved += amount;
    }

    public void confirm(int amount) {
        amountReserved -= amount;
        amountSpent += amount;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amountAvailable=" + amountAvailable +
                ", amountReserved=" + amountReserved +
                ", amountSpent=" + amountSpent +
                '}';
    }
}
