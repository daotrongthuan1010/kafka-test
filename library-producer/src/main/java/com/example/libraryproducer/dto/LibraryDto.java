package com.example.libraryproducer.dto;

import com.example.libraryproducer.entity.Book;
import com.example.libraryproducer.entity.LibraryEventType;

public record LibraryDto(String libraryEventId, LibraryEventType libraryEventType, Book book) {
};
