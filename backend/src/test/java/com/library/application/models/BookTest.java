package com.library.application.models;

import com.library.application.StartDatabaseTest;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookTest {
    private static Database database;

    /**
     * Carrega as configurações do banco de dados antes de iniciar o teste
     */
    @BeforeAll
    public static void setUp() {
        database = StartDatabaseTest.databaseTestSetup();
    }

    /**
     * Esse teste visa inserir, buscar e deletar um livro dentro do banco de dados de testes e verificar se o livro foi salvo corretamente
     */
    @Test
    public void insertFindDeleteBook() {
        Book book = new Book();
        book.setIdBook(1);
        book.setDsBookName("Manifesto Comunista");

        database.save(book);

        Book foundBook = database.find(Book.class, 1);

        assertThat(foundBook.getIdBook()).isEqualTo(book.getIdBook());
        assertThat(foundBook.getDsBookName()).isEqualTo(book.getDsBookName());

        database.delete(book);
    }
}