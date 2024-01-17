package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderSource;
import com.example.orderservice.domain.OrderStatus;
import org.springframework.stereotype.Service;

/**
 * The order service.
 * @version 1.0
 * @see OrderService
 * @author ThuanDao1010
 */
@Service
public class OrderServiceImpl implements OrderService{


    /**
     * This method is used to confirm the order.
     * If both the payment and stock services accept the order, the order is confirmed.
     * If one of the services rejects the order, the order is rejected.
     * If one of the services rejects the order, the other service is notified to rollback the order.
     * @param orderPayment
     * @param orderStock
     * @return
     */
    @Override
    public Order confirm(Order orderPayment, Order orderStock) {

        Order o = Order.builder()
                .id(orderPayment.getId())
                .customerId(orderPayment.getCustomerId())
                .productId(orderPayment.getProductId())
                .productCount(orderPayment.getProductCount())
                .price(orderPayment.getPrice())
                .build();

        if (orderPayment.getStatus().equals(OrderStatus.ACCEPT) &&
                orderStock.getStatus().equals(OrderStatus.ACCEPT)) {
            o.setStatus(OrderStatus.CONFIRMED);
        } else if (orderPayment.getStatus().equals(OrderStatus.REJECT) &&
                orderStock.getStatus().equals(OrderStatus.REJECT)) {
            o.setStatus(OrderStatus.REJECTED);
        } else if (orderPayment.getStatus().equals(OrderStatus.REJECT) ||
                orderStock.getStatus().equals(OrderStatus.REJECT)) {
            OrderSource source = orderPayment.getStatus().equals(OrderStatus.REJECT)
                    ? OrderSource.PAYMENT : OrderSource.STOCK;
            o.setStatus(OrderStatus.ROLLBACK);
            o.setSource(source);
        }
        return o;
    }

}
