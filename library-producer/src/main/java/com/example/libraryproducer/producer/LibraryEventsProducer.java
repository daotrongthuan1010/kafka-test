package com.example.libraryproducer.producer;

import com.example.libraryproducer.entity.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryEventsProducer {

    private final  KafkaTemplate<Integer, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic}")
    private String topic;


    public void sendMail(LibraryEvent event) throws JsonProcessingException {

        var key = event.libraryEventId();

        var value = objectMapper.writeValueAsString(event);


        CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.send(topic, key, value);

        completableFuture.whenComplete((sendResult, throwable) ->{
            if(!Objects.isNull(throwable)){
                handlerFailed(key, value, throwable);
            }
            handlerSuccess(key, value, sendResult);
        });


    }

    private void handlerSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Send producer success with key : {}, value : {}, partitions: {}",
                key,
                value,
                sendResult.getRecordMetadata().partition());
    }

    private void handlerFailed(Integer key, String value, Throwable throwable) {

        log.info("Send producer failed with key : {}, value: {}, error: {}",
                key,
                value,
                throwable.getMessage());
    }


}
