package com.example.libraryproducer.controller;

import com.example.libraryproducer.dto.LibraryDto;
import com.example.libraryproducer.entity.Book;
import com.example.libraryproducer.entity.LibraryEvent;
import com.example.libraryproducer.entity.LibraryEventType;
import com.example.libraryproducer.producer.LibraryEventsProducer;
import com.example.libraryproducer.producer.LibraryObjectProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/library/producer")
public class LibraryEventController {

    private final LibraryEventsProducer producer;

    private final LibraryObjectProducer libraryObjectProducer;

    @PostMapping("/create")
    public ResponseEntity<LibraryDto> createLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {

        log.info("libraryEvent start: {}",libraryEvent);

        producer.sendMail(libraryEvent);

        LibraryDto libraryDto = new LibraryDto(
                String.valueOf(libraryEvent.libraryEventId()),
                libraryEvent.libraryEventType(),
                libraryEvent.book());

        log.info("Success send producer !!!");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryDto);
    }

    @PostMapping("/create2")
    public ResponseEntity<LibraryDto> createLibraryEvent2(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        log.info("libraryEvent start: {}",libraryEvent);

        producer.sendMail2(libraryEvent);

        LibraryDto libraryDto = new LibraryDto(
                String.valueOf(libraryEvent.libraryEventId()),
                libraryEvent.libraryEventType(),
                libraryEvent.book());

        log.info("Success send producer !!!");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryDto);
    }

    @PostMapping("/create3")
    public ResponseEntity<LibraryDto> createLibraryEvent3(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        log.info("libraryEvent start: {}",libraryEvent);

        producer.sendMail3(libraryEvent);

        LibraryDto libraryDto = new LibraryDto(
                String.valueOf(libraryEvent.libraryEventId()),
                libraryEvent.libraryEventType(),
                libraryEvent.book());

        log.info("Success send producer !!!");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryDto);
    }

    @PostMapping("/create4")
    public ResponseEntity<LibraryDto> createLibraryEvent4(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {

        log.info("libraryEvent start: {}",libraryEvent);

        libraryObjectProducer.sendMailTest(libraryEvent);

        LibraryDto libraryDto = new LibraryDto(
                String.valueOf(libraryEvent.libraryEventId()),
                libraryEvent.libraryEventType(),
                libraryEvent.book());

        log.info("Success send producer !!!");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryDto);
    }

}
