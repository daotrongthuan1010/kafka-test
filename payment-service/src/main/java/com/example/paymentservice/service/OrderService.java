package com.example.paymentservice.service;

import com.example.paymentservice.domain.Order;

public interface OrderService {

    void reserve(Order order);

    void confirm(Order order);
}
