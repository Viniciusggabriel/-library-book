package com.library.application.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.application.dto.responses.BookResponse;
import com.library.application.models.Book;
import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.database.repositories.contracts.BookRepository;
import com.library.util.errors.exceptions.MalformedJsonException;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

@RequiredArgsConstructor
public class BookCrudService {
    private final BookRepository bookRepository;

    public BookCrudService() {
        this.bookRepository = new BookRepository(DataBaseSourceConfig.getDatabase());
    }

    public char[] getBookById(Long idBook) throws JsonProcessingException {
        Book bookInDatabase = bookRepository.selectEntityById(idBook);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json;

        try {
            json = objectMapper.writeValueAsString(
                    BookResponse.of(
                            bookInDatabase.getIdBook(),
                            bookInDatabase.getDsQuantityBooks(),
                            bookInDatabase.getDsBookName(),
                            bookInDatabase.getDsAuthorName(),
                            bookInDatabase.getDsReleaseDate(),
                            bookInDatabase.getDsSummary()
                    )
            );
        } catch (JsonProcessingException exception) {
            throw new MalformedJsonException(exception.getMessage(), exception.getLocation(), HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        return json.toCharArray();
    }
}
