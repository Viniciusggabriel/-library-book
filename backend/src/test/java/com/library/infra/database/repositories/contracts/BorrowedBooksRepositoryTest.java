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
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BorrowedBooksRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BorrowedBooksRepositoryTest.class);

    private BorrowedBooksRepository borrowedBooksRepository;

    private Database database;
    private ClientInLibrary clientInLibrary;
    private Book book;

    /**
     * <h2>Instancia antes do teste a configuração de banco de dados e do repositório a ser testado</h2>
     */
    @BeforeEach
    void setUp() {
        database = DataBaseSourceConfigTest.databaseTestSetup(new Class[]{Book.class, BorrowedBooks.class, UserInLibrary.class});
        borrowedBooksRepository = new BorrowedBooksRepository(database);
        setUpTestData();
    }

    /**
     * <h3>Método para realizar a pré-configuração das dependências necessárias para testar a entidade BorrowedBooks.</h3>
     * <p>Este método cria um usuário e um livro como exemplos, que serão utilizados durante os testes da entidade
     * de empréstimos.</p>
     */
    private void setUpTestData() {
        // Configurar o livro
        book = new Book();
        book.setIdBook(1L);
        book.setDsBookName("Manifesto Comunista");
        book.setDsAuthorName("Karl Marx");
        book.setDsSummary("Livro sobre ideia econômica");
        book.setDsReleaseDate(ZonedDateTime.now().withNano(0));
        book.setDsQuantityBooks(1);

        // Verifica se o livro já existe dentro do banco de dados de testes
        Optional<Book> existingBook = Optional.ofNullable(database.find(Book.class, 1L));
        existingBook.ifPresent(bookIsPresent -> {
            logger.debug("O livro já existia no banco de dados: {}", bookIsPresent.getDsBookName());
        });

        // Caso não exista cria
        database.save(book);
        logger.info("Livro salvo dentro do banco com sucesso!");

        // Configurar o usuário
        UUID USER_UUID = UUID.randomUUID();
        UserInLibrary userInLibrary = new UserInLibrary(USER_UUID, "Testador", "Senha em plain text");

        // Verifica se o usuário já existe dentro do banco de dados de testes
        Optional<UserInLibrary> existingUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, USER_UUID));
        existingUserInLibrary.ifPresent(userIsPresent -> {
            logger.debug("O usuário já existia no banco de dados: {}", userIsPresent.getDsUserName());
        });

        // Caso não exista cria
        database.save(userInLibrary);
        logger.info("Usuário salvo novamente no banco de dados com sucesso!");

        // Configura um cliente
        UUID CLIENT_UUID = UUID.randomUUID();
        clientInLibrary = new ClientInLibrary(
                CLIENT_UUID,
                "TestadorCliente",
                "62987654321",
                "testador@email.com",
                ZonedDateTime.now(),
                userInLibrary
        );

        // Verifica se o cliente já existe dentro do banco de dados de testes
        Optional<ClientInLibrary> existingClientInLibrary = Optional.ofNullable(database.find(ClientInLibrary.class, CLIENT_UUID));
        existingClientInLibrary.ifPresent(clientIsPresent -> {
            logger.debug("O cliente já existia no banco de dados: {}", clientIsPresent.getDsClientName());
        });

        // Caso não exista cria
        database.save(clientInLibrary);
        logger.info("Usuário salvo novamente no banco de dados com sucesso!");
    }

    @Test
    public void insertUpdateFindDeleteBorrowedBooks() {
        BorrowedBooks borrowedBooks = new BorrowedBooks(1L, ZonedDateTime.now().withNano(0), ZonedDateTime.now().withNano(0).plusWeeks(1), clientInLibrary, List.of(book));
        logger.info("O objeto de empréstimo foi criado!");

        borrowedBooksRepository.insertEntity(borrowedBooks);
        logger.info("O empréstimo foi salvo com sucesso!");

        BorrowedBooks bookForUpdate = new BorrowedBooks();
        bookForUpdate.setDsExpectedDeliveryDate(ZonedDateTime.now().withNano(0).plusWeeks(3));

        borrowedBooksRepository.updateEntity(bookForUpdate, 1L);
        logger.info("O livro foi alterado com sucesso!");

        Optional<BorrowedBooks> user = Optional.ofNullable(borrowedBooksRepository.selectEntityById(1L));
        user.ifPresent(bookIsPresent -> {
            assertThat(bookIsPresent.getDsBorrowedDate()).isEqualTo(borrowedBooks.getDsBorrowedDate());
            assertThat(bookIsPresent.getDsExpectedDeliveryDate()).isEqualTo(bookForUpdate.getDsExpectedDeliveryDate());
            assertThat(bookIsPresent.getIdBorrowed()).isEqualTo(borrowedBooks.getIdBorrowed());
            assertThat(bookIsPresent.getFkIdBook()).isEqualTo(borrowedBooks.getFkIdBook());
            assertThat(bookIsPresent.getFkIdClientInLibrary()).isEqualTo(borrowedBooks.getFkIdClientInLibrary());
        });
        logger.info("O empréstimo foi achado com sucesso pelo seu ID!");

        borrowedBooksRepository.deleteEntity(1L);
        logger.info("O empréstimo foi deletado com sucesso!");

        assertThatThrownBy(() -> {
            borrowedBooksRepository.selectEntityById(1L);
            logger.info("Confirmação: O empréstimo foi removido corretamente do banco de dados.");
        })
                .isInstanceOf(ValueNotFoundException.class)
                .hasMessageContaining("Não foi possível encontrar o empréstimo de livro!")
                .hasMessageContaining("status: 404");
    }

    /**
     * <h3>Teste para verificar se a busca de vários itens dentro do banco de dados está sendo executada com sucesso</h3>
     */
    @Test
    public void selectAllBooks() {
        List<BorrowedBooks> borrowedBooks = new ArrayList<>();

        BorrowedBooks borrowedBook;
        for (int value = 1; value <= 10; value++) {
            borrowedBook = new BorrowedBooks((long) value, ZonedDateTime.now(), ZonedDateTime.now().plusWeeks(1), clientInLibrary, List.of(book));

            borrowedBooks.add(borrowedBook);
        }

        // Insere os livros gerados pelo for
        borrowedBooks.forEach(bookInArray -> {
            borrowedBooksRepository.insertEntity(bookInArray);
        });

        List<BorrowedBooks> borrowedBooksList = borrowedBooksRepository.selectEntities(6,1);
        borrowedBooksList.forEach(bookInArray -> logger.info("Empréstimos foram listados de forma correta: {}", bookInArray));
    }
}