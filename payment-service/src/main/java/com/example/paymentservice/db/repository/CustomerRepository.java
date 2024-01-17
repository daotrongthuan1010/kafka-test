package com.example.paymentservice.db.repository;

import com.example.paymentservice.db.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}