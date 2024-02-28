package com.example.libraryproducer.producer;

import com.example.libraryproducer.entity.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryObjectProducer {

    private final KafkaTemplate<String, LibraryEvent> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String topic;


    public void sendMailTest(LibraryEvent event) throws JsonProcessingException {

        var key = event.libraryEventId().toString();


        CompletableFuture<SendResult<String,LibraryEvent>> completableFuture = kafkaTemplate.send(topic, key, event);

        completableFuture.whenComplete((sendResult, throwable) ->{
            if(!Objects.isNull(throwable)){
                System.out.println("That Bai");
            }
            System.out.println("Thanh cong");
        });
    }

}

