package com.library.dao.entities;

import io.ebean.DB;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    public void insertFindDeleteBook() {
        Book book = new Book();
        book.setDsBookName("Naruto");

        DB.save(book);

        Book foundBook = DB.find(Book.class, 1);

        DB.delete(book);
    }
}