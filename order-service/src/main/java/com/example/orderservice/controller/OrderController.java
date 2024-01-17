package com.example.orderservice.controller;


import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.domain.Topics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class is used to handle all the requests related to orders.
 *
 * @since 1.0.0
 * @author DaoThuan1010
 */
@Slf4j
@RequestMapping("/orders")
@RestController
public class OrderController {


    private final AtomicLong id = new AtomicLong();
    private final KafkaTemplate<Long, Order> kafkaTemplate;
    private final StreamsBuilderFactoryBean kafkaStreamsFactory;


    @Autowired
    public OrderController(KafkaTemplate<Long, Order> kafkaTemplate,
                           StreamsBuilderFactoryBean kafkaStreamsFactory) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaStreamsFactory = kafkaStreamsFactory;
    }

    /**
     * This method is used to create a new order.
     * @param order
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping
    public Order create(@RequestBody Order order) throws ExecutionException, InterruptedException {
        order.setId(id.incrementAndGet());
        order.setStatus(OrderStatus.NEW);
        log.info("Sent: {}", order);
        return kafkaTemplate.send(Topics.ORDERS, order.getId(), order).get().getProducerRecord().value();
    }

    /**
     * This method is used to get all orders from the local state store.
     * @return
     */
    @GetMapping
    public List<Order> all() {
        List<Order> orders = new ArrayList<>();
        ReadOnlyKeyValueStore<Long, Order> store = kafkaStreamsFactory
                .getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        Topics.ORDERS,
                        QueryableStoreTypes.keyValueStore()));
        KeyValueIterator<Long, Order> it = store.all();
        it.forEachRemaining(kv -> orders.add(kv.value));
        return orders;
    }
}
