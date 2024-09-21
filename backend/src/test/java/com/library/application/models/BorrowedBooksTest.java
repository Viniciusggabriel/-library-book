package com.library.application.models;

import com.library.application.StartDatabaseTest;
import io.ebean.Database;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
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
        database = StartDatabaseTest.databaseTestSetup();
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

        // Limpa o livro se já existe
        if (Objects.equals(database.find(Book.class, 1), book)) database.delete(book);

        database.save(book);
        logger.info("Livro salvo dentro do banco com sucesso!");

        // Configurar o usuário
        UUID USER_UUID = UUID.randomUUID();

        userInLibrary = new UserInLibrary();
        userInLibrary.setIdUser(USER_UUID);
        userInLibrary.setDsUserName("Testador");
        userInLibrary.setDsPassword("Senha em plain text");

        // Limpa o usuário se já existe
        if (Objects.equals(database.find(UserInLibrary.class, USER_UUID), userInLibrary))
            database.delete(userInLibrary);

        database.save(userInLibrary);
        logger.info("Usuário salvo no banco de dados com sucesso!");
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

        BorrowedBooks existingBorrowedBooks = database.find(BorrowedBooks.class, borrowedBooks.getIdBorrowed());
        if (existingBorrowedBooks != null) {
            database.delete(existingBorrowedBooks);
            logger.debug("O empréstimo já existia no banco de dados, ele foi apagado!!");
        }

        database.save(borrowedBooks);
        logger.info("O empréstimo de livro foi persistido com sucesso!");

        BorrowedBooks foundBorrowedBooks = database.find(BorrowedBooks.class, 1);
        logger.info("O empréstimo foi encontrado com sucesso!");

        assertThat(foundBorrowedBooks.getIdBorrowed()).isEqualTo(borrowedBooks.getIdBorrowed());
        assertThat(foundBorrowedBooks.getDsBorrowedDate()).isEqualTo(borrowedBooks.getDsBorrowedDate());
        assertThat(foundBorrowedBooks.getDsExpectedDeliveryDate()).isEqualTo(borrowedBooks.getDsExpectedDeliveryDate());
        assertThat(foundBorrowedBooks.getFkIdBook()).isEqualTo(borrowedBooks.getFkIdBook());
        assertThat(foundBorrowedBooks.getFkIdUserInLibrary()).isEqualTo(borrowedBooks.getFkIdUserInLibrary());

        try {
            database.delete(borrowedBooks);
        } catch (Exception exception) {
            logger.error("Erro ao deletar o empréstimo: ", exception);
        }
    }
}
