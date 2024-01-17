package com.example.orderservice.service;

import com.example.orderservice.domain.Order;

/**
 * The order service.
 * @version 1.0
 * @author DaoThuan1010
 */
public interface OrderService {

    /**
     * Reserve the order.
     *
     * @param orderPayment the order to reserve
     * @param orderStock the order to reserve
     * @return the reserved order
     */
    Order confirm(Order orderPayment, Order orderStock);
}
