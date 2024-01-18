package com.example.orderservice.config;


import com.example.orderservice.domain.Order;
import com.example.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

import static com.example.orderservice.domain.Topics.*;

/**
 * This class is used to configure Kafka.
 *
 * @since 1.0.0
 * @author DaoThuan1010
 */
@Slf4j
@Configuration
public class KafkaConfig {


    private final OrderService orderService;


    @Autowired
    public KafkaConfig(OrderService orderService) {
        this.orderService = orderService;
    }

    @Bean
    public NewTopic orders() {
        return TopicBuilder.name(ORDERS)
                .partitions(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name(PAYMENTS)
                .partitions(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic stockTopic() {
        return TopicBuilder.name(STOCK)
                .partitions(3)
                .compact()
                .build();
    }

    /**
     * This is the one that will send results to other topics
     * @param builder
     * @return
     */
    @Bean
    public KStream<Long, Order> stream(StreamsBuilder builder) {

        Serde<Long> keySerde =  Serdes.Long();  // Serdes.LongSerde keySerde=new Serdes.LongSerde();
        JsonSerde<Order> valueSerde = new JsonSerde<>(Order.class);

        //Create a stream from the topic
        KStream<Long, Order> paymentStream = builder
                .stream(PAYMENTS, Consumed.with(keySerde, valueSerde));//Consumed With == passing some parameters for configuring the generated stream

        KStream<Long, Order> stockStream = builder
                .stream(STOCK,Consumed.with(keySerde, valueSerde));

        //Join records from both tables
        paymentStream.join(
                stockStream,
                orderService::confirm,  //The value joiner is the one responsible for joining the two records
                JoinWindows.of(Duration.ofSeconds(10)), //Timestamps of matched records must fall within this window of time
                StreamJoined.with(keySerde, valueSerde, valueSerde) //The key must be the same, 1st stream serde, 2nd stream serde
                  )
                .peek((k,v)->log.info("Kafka stream match: key[{}],value[{}]",k,v))
                .to(ORDERS);

        return paymentStream;
    }

    /**
     * This is the one that will get results from other topics to check
     * @param builder
     * @return
     */

    @Bean
    public KTable<Long, Order> table(StreamsBuilder builder) {

        KeyValueBytesStoreSupplier store = Stores.persistentKeyValueStore(ORDERS);

        Serde<Long> keySerde =  Serdes.Long();
        JsonSerde<Order> valueSerde = new JsonSerde<>(Order.class);

        KStream<Long, Order> stream = builder
                .stream(ORDERS, Consumed.with(keySerde, valueSerde))
                .peek((k,v)->log.info("Kafka persistence table: key[{}],value[{}]",k,v));

        return stream.toTable(Materialized.<Long, Order>as(store)
                .withKeySerde(keySerde)
                .withValueSerde(valueSerde));
    }



    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("kafkaSender-");
        executor.initialize();
        return executor;
    }
}
