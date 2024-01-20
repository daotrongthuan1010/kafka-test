package com.example.paymentservice.service;


import com.example.paymentservice.db.entities.Customer;
import com.example.paymentservice.db.repository.CustomerRepository;
import com.example.paymentservice.domain.Order;
import com.example.paymentservice.domain.OrderSource;
import com.example.paymentservice.domain.OrderStatus;
import com.example.paymentservice.domain.Topics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    private static final OrderSource SOURCE = OrderSource.PAYMENT;
    private final CustomerRepository repository;
    private final KafkaTemplate<Long, Order> template;

    public OrderServiceImpl(CustomerRepository repository, KafkaTemplate<Long, Order> template) {
        this.repository = repository;
        this.template = template;
    }

    @Override
    public void reserve(Order order) {
        Customer customer = repository.findById(order.getCustomerId()).orElseThrow();
        log.info("reserve order [{}] , for customer[{}]",order.getId(), customer);
        if (order.getPrice() < customer.getAmountAvailable()) {
            order.setStatus(OrderStatus.ACCEPT);
            customer.setAmountReserved(customer.getAmountReserved() + order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getPrice());
        } else {
            order.setStatus(OrderStatus.REJECTED);
        }
        order.setSource(SOURCE);
        repository.save(customer);
        template.send(Topics.PAYMENTS, order.getId(), order);
        log.info("Sent: {}", order);
    }

    @Override
    public void confirm(Order order) {
        Customer customer = repository.findById(order.getCustomerId()).orElseThrow();
        log.info("confirm order [{}] , for customer[{}]",order.getId(), customer);
        if (order.getStatus().equals(OrderStatus.CONFIRMED)) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            repository.save(customer);
        } else if (order.getStatus().equals(OrderStatus.ROLLBACK) && !order.getSource().equals(SOURCE)) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() + order.getPrice());
            repository.save(customer);
        }

    }

    @Override
    public void setOrderStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        template.send(Topics.PAYMENTS, order.getId(), order);
        log.info("Sent: {}", order);
    }
}
