package com.library.application.models;

import com.library.application.StartDatabaseTest;
import io.ebean.Database;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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
        book.setIdBook(1L);
        book.setDsBookName("Manifesto Comunista");
        book.setDsAuthorName("Karl Marx");
        book.setDsSummary("Livro sobre ideia econômica");
        book.setDsReleaseDate(LocalDateTime.now());
        book.setDsQuantityBooks(1);

        // Limpa o livro se já existe
        if (Objects.equals(database.find(UserInLibrary.class, 1), book)) {
            database.delete(book);
            logger.debug("O livro já existia no banco de dados, ele foi apagado!");
        }

        database.save(book);
        logger.info("Livro salvo dentro do banco com sucesso!");

        Book foundBook = database.find(Book.class, 1);
        logger.info("Livro encontrado dentro do banco de dados!");

        assertThat(foundBook.getIdBook()).isEqualTo(book.getIdBook());
        assertThat(foundBook.getDsBookName()).isEqualTo(book.getDsBookName());
        assertThat(foundBook.getDsAuthorName()).isEqualTo(book.getDsAuthorName());
        assertThat(foundBook.getDsSummary()).isEqualTo(book.getDsSummary());
        assertThat(foundBook.getDsReleaseDate()).isEqualTo(book.getDsReleaseDate());
        assertThat(foundBook.getDsQuantityBooks()).isEqualTo(book.getDsQuantityBooks());

        database.delete(book);
        logger.info("Livro deletado do banco com sucesso!");
    }
}