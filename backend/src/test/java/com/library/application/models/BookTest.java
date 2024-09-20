package com.library.application.models;

import com.library.application.StartDatabaseTest;
import com.library.util.errors.handlers.ServerErrorHttpHandler;
import io.ebean.Database;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookTest {
    private static final Logger logger = LoggerFactory.getLogger(BookTest.class);

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
        book.setDsAuthorName("Karl Marx");
        book.setDsSummary("Livro sobre ideia econômica");
        book.setDsReleaseDate(LocalDateTime.now());

        database.save(book);
        logger.info("Objeto do livro instanciado e salvo dentro do banco com sucesso!");

        Book foundBook = database.find(Book.class, 1);
        logger.info("Livro encontrado dentro do banco de dados!");

        assertThat(foundBook.getIdBook()).isEqualTo(book.getIdBook());
        assertThat(foundBook.getDsBookName()).isEqualTo(book.getDsBookName());
        assertThat(foundBook.getDsAuthorName()).isEqualTo(book.getDsAuthorName());
        assertThat(foundBook.getDsSummary()).isEqualTo(book.getDsSummary());
        assertThat(foundBook.getDsReleaseDate()).isEqualTo(book.getDsReleaseDate());

        database.delete(book);
        logger.info("Livro deletado do banco com sucesso!");
    }
}