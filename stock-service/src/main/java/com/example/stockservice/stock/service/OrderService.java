package com.example.stockservice.stock.service;

import com.example.stockservice.stock.domain.Order;

/**
 * The order service.
 * @version 1.0
 * @author DaoThuan1010
 */
public interface OrderService {

    void reserve(Order order);

    void confirm(Order order);
}
