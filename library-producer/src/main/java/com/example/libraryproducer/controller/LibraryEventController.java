package com.example.libraryproducer.controller;

import com.example.libraryproducer.dto.LibraryDto;
import com.example.libraryproducer.entity.Book;
import com.example.libraryproducer.entity.LibraryEvent;
import com.example.libraryproducer.entity.LibraryEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/library/producer")
public class LibraryEventController {

    @PostMapping("/create")
    public ResponseEntity<LibraryDto> createLibraryEvent(@RequestBody LibraryEvent libraryEvent){

        log.info("libraryEvent start: {}",libraryEvent);

        LibraryDto libraryDto = new LibraryDto(
                libraryEvent.libraryEventId(),
                LibraryEventType.NEW,
                new Book("123","Book1", "ThuanDT3"));

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryDto);
    }

}
