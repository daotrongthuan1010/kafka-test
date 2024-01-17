package com.example.stockservice.stock.service;

import com.example.stockservice.stock.db.entities.Product;
import com.example.stockservice.stock.db.repository.ProductRepository;
import com.example.stockservice.stock.domain.Order;
import com.example.stockservice.stock.domain.OrderSource;
import com.example.stockservice.stock.domain.OrderStatus;
import com.example.stockservice.stock.domain.Topics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


/**
 * The order service.
 * @version 1.0
 * @see OrderService
 * @see Order
 * @see Product
 * @see ProductRepository
 * @see KafkaTemplate
 * @see Topics
 * @see OrderSource
 * @see OrderStatus
 * @see Service
 * @see Slf4j
 * @author DaoThuan1010
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    private static final OrderSource SOURCE = OrderSource.STOCK;
    private final ProductRepository repository;
    private final KafkaTemplate<Long, Order> template;

    public OrderServiceImpl(ProductRepository repository, KafkaTemplate<Long, Order> template) {
        this.repository = repository;
        this.template = template;
    }

    @Override
    public void reserve(Order order) {
        Product product = repository.findById(order.getProductId()).orElseThrow();
        log.info("Found: {}", product);
        if (order.getStatus().equals(OrderStatus.NEW)) {
            if (order.getProductCount() < product.getAvailableItems()) {
                product.setReservedItems(product.getReservedItems() + order.getProductCount());
                product.setAvailableItems(product.getAvailableItems() - order.getProductCount());
                order.setStatus(OrderStatus.ACCEPT);
                repository.save(product);
            } else {
                order.setStatus(OrderStatus.REJECT);
            }
            template.send(Topics.STOCK, order.getId(), order);
            log.info("Sent: {}", order);
        }
    }

    @Override
    public void confirm(Order order) {
        Product product = repository.findById(order.getProductId()).orElseThrow();
        log.info("Found: {}", product);
        if (order.getStatus().equals(OrderStatus.CONFIRMED)) {
            product.setReservedItems(product.getReservedItems() - order.getProductCount());
            repository.save(product);
        } else if (order.getStatus().equals(OrderStatus.ROLLBACK) && !order.getSource().equals(SOURCE)) {
            product.setReservedItems(product.getReservedItems() - order.getProductCount());
            product.setAvailableItems(product.getAvailableItems() + order.getProductCount());
            repository.save(product);
        }
    }

}
