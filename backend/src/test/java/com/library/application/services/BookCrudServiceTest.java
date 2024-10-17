package com.library.application.services;

import com.library.application.dto.requests.BookRequest;
import com.library.util.errors.exceptions.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

class BookCrudServiceTest {
    private BookCrudService bookCrudService;

    @BeforeEach
    void setUp() {
        bookCrudService = new BookCrudService();
    }

    @Test
    public void crudService() {
    }

    @Test
    void postBook() {

    }

    @Test
    public void getBookByID() throws MalformedJsonException {

    }
}