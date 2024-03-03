package com.example.libraryconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface LibraryConsumerService {

    void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException, IllegalAccessException;
}
