package com.library.application.models;

import com.library.application.DataBaseSourceConfigTest;
import io.ebean.Database;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BorrowedBooksTest {
    private static final Logger logger = LoggerFactory.getLogger(BorrowedBooksTest.class);

    private Database database;
    private Book book;
    private UserInLibrary userInLibrary;

    /**
     * Carrega as configurações do banco de dados antes de iniciar o teste
     */
    @BeforeEach
    public void setUp() {
        database = DataBaseSourceConfigTest.databaseTestSetup(List.of(Book.class, UserInLibrary.class, BorrowedBooks.class));
        setUpTestData();
    }

    /**
     * Método para realizar a pré-configuração das dependências necessárias para testar a entidade BorrowedBooks.
     * <p>
     * Este método cria um usuário e um livro como exemplos, que serão utilizados durante os testes da entidade
     * de empréstimos.
     * </p>
     */
    private void setUpTestData() {
        // Configurar o livro
        book = new Book();
        book.setIdBook(1L);
        book.setDsBookName("Manifesto Comunista");
        book.setDsAuthorName("Karl Marx");
        book.setDsSummary("Livro sobre ideia econômica");
        book.setDsReleaseDate(LocalDateTime.now());
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

        userInLibrary = new UserInLibrary();
        userInLibrary.setIdUser(USER_UUID);
        userInLibrary.setDsUserName("Testador");
        userInLibrary.setDsPassword("Senha em plain text");

        // Verifica se o usuário já existe dentro do banco de dados de testes
        Optional<UserInLibrary> existingUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, USER_UUID));
        existingUserInLibrary.ifPresent(userInLibraryIsPresent -> {
            logger.debug("O usuário já existia no banco de dados: {}", userInLibraryIsPresent.getDsUserName());
        });

        // Caso não exista cria
        database.save(userInLibrary);
        logger.info("Usuário salvo novamente no banco de dados com sucesso!");
    }

    /**
     * <h1>Método de teste para operações de insert, find e delete na entidade de empréstimo.</h1>
     * <p>
     * Este teste instancia um objeto de empréstimo e verifica se ele já existe no banco de dados de testes.
     * Se o objeto já existir, ele será deletado, e o teste será considerado bem-sucedido. Caso contrário, o
     * teste continuará sua execução normalmente.
     * </p>
     * <p>
     * A execução padrão envolve a criação do objeto, a busca do usuário associado e a comparação dos valores
     * encontrados. Após a validação, o objeto é removido do banco de dados.
     * </p>
     */
    @Test
    public void insertFindDeleteBorrowedBooks() {
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        borrowedBooks.setIdBorrowed(1L);
        borrowedBooks.setDsBorrowedDate(LocalDateTime.now());
        borrowedBooks.setDsExpectedDeliveryDate(LocalDateTime.now().plusWeeks(1));
        borrowedBooks.setFkIdBook(List.of(book));
        borrowedBooks.setFkIdUserInLibrary(userInLibrary);

        database.save(borrowedBooks);
        logger.info("O empréstimo de livro foi persistido com sucesso!");

        Optional<BorrowedBooks> existingUserInLibrary = Optional.ofNullable(database.find(BorrowedBooks.class, 1L));
        existingUserInLibrary.ifPresent(borrowedBooksIsPresent -> {
            assertThat(borrowedBooksIsPresent.getIdBorrowed()).isEqualTo(borrowedBooks.getIdBorrowed());
            assertThat(borrowedBooksIsPresent.getDsBorrowedDate()).isEqualTo(borrowedBooks.getDsBorrowedDate());
            assertThat(borrowedBooksIsPresent.getDsExpectedDeliveryDate()).isEqualTo(borrowedBooks.getDsExpectedDeliveryDate());
            assertThat(borrowedBooksIsPresent.getFkIdBook()).isEqualTo(borrowedBooks.getFkIdBook());
            assertThat(borrowedBooksIsPresent.getFkIdUserInLibrary()).isEqualTo(borrowedBooks.getFkIdUserInLibrary());
        });
        logger.info("O empréstimo foi encontrado com sucesso!");

        database.delete(borrowedBooks);
        logger.info("O empréstimo foi removido com sucesso do banco de dados!");

        BorrowedBooks deletedBook = database.find(BorrowedBooks.class, 1L);
        assertThat(deletedBook).isNull();
        logger.info("Confirmação: O empréstimo foi removido corretamente do banco de dados.");
    }
}
