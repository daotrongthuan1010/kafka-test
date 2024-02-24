package com.example.libraryproducer.controller;

import com.example.libraryproducer.dto.LibraryDto;
import com.example.libraryproducer.entity.Book;
import com.example.libraryproducer.entity.LibraryEvent;
import com.example.libraryproducer.entity.LibraryEventType;
import com.example.libraryproducer.producer.LibraryEventsProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/library/producer")
public class LibraryEventController {

    private final LibraryEventsProducer producer;

    @PostMapping("/create")
    public ResponseEntity<LibraryDto> createLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {

        log.info("libraryEvent start: {}",libraryEvent);

        producer.sendMail(libraryEvent);

        LibraryDto libraryDto = new LibraryDto(
                String.valueOf(libraryEvent.libraryEventId()),
                libraryEvent.libraryEventType(),
                libraryEvent.book());

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryDto);
    }

}
