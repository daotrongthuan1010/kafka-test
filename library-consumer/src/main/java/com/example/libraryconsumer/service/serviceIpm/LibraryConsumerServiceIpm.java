package com.example.libraryconsumer.service.serviceIpm;

import com.example.libraryconsumer.entity.LibraryEvent;
import com.example.libraryconsumer.repository.LibraryEventsRepository;
import com.example.libraryconsumer.service.LibraryConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryConsumerServiceIpm implements LibraryConsumerService {

    private final ObjectMapper objectMapper;

    private final LibraryEventsRepository repository;

    @Override
    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException, IllegalAccessException {

        LibraryEvent libraryEvent =
                objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);

        log.info("libraryEvent: {}",libraryEvent);

        switch (libraryEvent.getLibraryEventType()){
            case "NEW":
                repository.save(libraryEvent);
                break;
            case "UPDATE":
                validate(libraryEvent);
                repository.save(libraryEvent);
               break;
        }
    }

    private void validate(LibraryEvent libraryEvent) throws IllegalAccessException {
        if(Objects.isNull(libraryEvent.getId())){
            throw new IllegalAccessException("Library Event Id iss missing");
        }
        repository.findById(Math.toIntExact(libraryEvent.getId()));
    }
}
