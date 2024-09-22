package com.library.application.models;

import com.library.application.DataBaseSourceConfigTest;
import io.ebean.Database;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookTest {
    private static final Logger logger = LoggerFactory.getLogger(BookTest.class);

    private static Database database;

    /**
     * Carrega as configurações do banco de dados antes de iniciar o teste
     */
    @BeforeAll
    public static void setUp() {
        database = DataBaseSourceConfigTest.databaseTestSetup(List.of(Book.class, BorrowedBooks.class, UserInLibrary.class));
    }

    /**
     * Esse teste visa inserir, buscar e deletar um livro dentro do banco de dados de testes e verificar se o livro foi salvo corretamente
     */
    @Test
    public void insertFindDeleteBook() {
        Book book = new Book();
        book.setIdBook(1L);
        book.setDsBookName("Manifesto Comunista");
        book.setDsAuthorName("Karl Marx");
        book.setDsSummary("Livro sobre ideia econômica");
        book.setDsReleaseDate(LocalDateTime.now());
        book.setDsQuantityBooks(1);

        database.save(book);
        logger.info("Livro salvo dentro do banco com sucesso!");

        Optional<Book> existingBook = Optional.ofNullable(database.find(Book.class, 1L));
        existingBook.ifPresent(bookIsPresent -> {
            assertThat(bookIsPresent.getIdBook()).isEqualTo(book.getIdBook());
            assertThat(bookIsPresent.getDsBookName()).isEqualTo(book.getDsBookName());
            assertThat(bookIsPresent.getDsAuthorName()).isEqualTo(book.getDsAuthorName());
            assertThat(bookIsPresent.getDsSummary()).isEqualTo(book.getDsSummary());
            assertThat(bookIsPresent.getDsReleaseDate()).isEqualTo(book.getDsReleaseDate());
            assertThat(bookIsPresent.getDsQuantityBooks()).isEqualTo(book.getDsQuantityBooks());
        });
        logger.info("Livro encontrado dentro do banco de dados!");

        database.delete(book);
        logger.info("Livro deletado do banco com sucesso!");

        Book deletedBook = database.find(Book.class, 1L);
        assertThat(deletedBook).isNull();
        logger.info("Confirmação: O livro foi removido corretamente do banco de dados.");
    }
}