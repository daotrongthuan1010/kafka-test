package com.example.libraryproducer.entity;

public record LibraryEvent (Integer libraryEventId, LibraryEventType libraryEventType, Book book){
};
