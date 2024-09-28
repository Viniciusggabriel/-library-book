package com.library.application.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.application.dto.responses.BookResponse;
import com.library.application.models.Book;
import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.database.repositories.contracts.BookRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookCrudService {
    private final BookRepository bookRepository;

    public BookCrudService() {
        this.bookRepository = new BookRepository(DataBaseSourceConfig.getDatabase());
    }

    public char[] getBookById(String idBook) throws JsonProcessingException {
        Book bookInDatabase = bookRepository.selectEntityById(Long.valueOf(idBook));

        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(bookInDatabase.getDsReleaseDate());

        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(
                BookResponse.of(
                        bookInDatabase.getIdBook(),
                        bookInDatabase.getDsQuantityBooks(),
                        bookInDatabase.getDsBookName(),
                        bookInDatabase.getDsAuthorName(),
                        bookInDatabase.getDsReleaseDate(),
                        bookInDatabase.getDsSummary()
                )
        );

        return json.toCharArray();
    }
}
