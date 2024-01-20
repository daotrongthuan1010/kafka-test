package com.example.paymentservice.service;

import com.example.paymentservice.domain.Order;
import com.example.paymentservice.domain.OrderStatus;

public interface OrderService {

    void reserve(Order order);

    void confirm(Order order);

    void setOrderStatus(Order order, OrderStatus status);
}
