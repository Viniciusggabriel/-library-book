package com.library.infra.database.repositories.contracts;

import com.library.DataBaseSourceConfigTest;
import com.library.application.models.Book;
import com.library.application.models.BorrowedBooks;
import com.library.application.models.ClientInLibrary;
import com.library.application.models.UserInLibrary;
import com.library.util.errors.exceptions.ValueNotFoundException;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BookRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryTest.class);

    private BookRepository bookRepository;

    /**
     * <h2>Instancia antes do teste a configuração de banco de dados e do repositório a ser testado</h2>
     */
    @BeforeEach
    void setUp() {
        Database database = DataBaseSourceConfigTest.databaseTestSetup(new Class[]{Book.class, BorrowedBooks.class, UserInLibrary.class, ClientInLibrary.class});
        bookRepository = new BookRepository(database);
    }

    /**
     * <h3>Teste para verificar o repositório de livros está funcionando de forma correta</h3>
     */
    @Test
    public void insertUpdateFindDeleteBook() {
        Book book = new Book();
        book.setIdBook(1L);
        book.setDsBookName("Manifesto Comunista");
        book.setDsAuthorName("Karl Marx");
        book.setDsSummary("Livro sobre ideia econômica");
        book.setDsReleaseDate(ZonedDateTime.now().withNano(0));
        book.setDsQuantityBooks(1);
        logger.info("O objeto de livro foi criado!");

        bookRepository.insertEntity(book);
        logger.info("O livro foi salvo com sucesso!");

        Book bookForUpdate = new Book();
        bookForUpdate.setDsBookName("Frans kafka");

        bookRepository.updateEntity(bookForUpdate, 1L);
        logger.info("O livro foi alterado com sucesso!");

        Optional<Book> user = Optional.ofNullable(bookRepository.selectEntityById(1L));
        user.ifPresent(bookIsPresent -> {
            assertThat(bookIsPresent.getIdBook()).isEqualTo(book.getIdBook());
            assertThat(bookIsPresent.getDsBookName()).isEqualTo(bookForUpdate.getDsBookName());
            assertThat(bookIsPresent.getDsAuthorName()).isEqualTo(book.getDsAuthorName());
            assertThat(bookIsPresent.getDsSummary()).isEqualTo(book.getDsSummary());
            assertThat(bookIsPresent.getDsReleaseDate()).isEqualTo(book.getDsReleaseDate());
            assertThat(bookIsPresent.getDsQuantityBooks()).isEqualTo(book.getDsQuantityBooks());
        });
        logger.info("O livro foi achado com sucesso pelo seu ID!");

        bookRepository.deleteEntity(1L);
        logger.info("O livro foi deletado com sucesso!");

        assertThatThrownBy(() -> {
            bookRepository.selectEntityById(1L);
            logger.info("Confirmação: O livro foi removido corretamente do banco de dados.");
        })
                .isInstanceOf(ValueNotFoundException.class)
                .hasMessageContaining("O livro não foi encontrado!");
    }

    /**
     * <h3>Teste para verificar se a busca de vários itens dentro do banco de dados está sendo executada com sucesso</h3>
     */
    @Test
    public void selectAllBooks() {
        List<Book> books = new ArrayList<>();

        Book book;
        for (int value = 1; value <= 10; value++) {
            book = new Book();
            book.setIdBook((long) value);
            book.setDsBookName("Livro " + value);
            book.setDsAuthorName("Autor " + value);
            book.setDsSummary("Resumo do livro " + value);
            book.setDsReleaseDate(ZonedDateTime.now().withNano(0));
            book.setDsQuantityBooks(1);

            books.add(book);
        }

        // Insere os livros gerados pelo for
        books.forEach(bookInArray -> {
            bookRepository.insertEntity(bookInArray);
        });

        List<Book> booksList = bookRepository.selectEntities(6, 1);
        booksList.forEach(bookInArray -> logger.info("Livros foram listados de forma correta: {}", bookInArray));
    }
}