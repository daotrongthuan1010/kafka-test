package com.example.libraryproducer.entity;

public record LibraryEvent (String libraryEventId, LibraryEventType libraryEventType, Book book){
};
