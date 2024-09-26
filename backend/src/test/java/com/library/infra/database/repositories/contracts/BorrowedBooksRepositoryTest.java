package com.library.infra.database.repositories.contracts;

import com.library.DataBaseSourceConfigTest;
import com.library.application.models.Book;
import com.library.application.models.BorrowedBooks;
import com.library.application.models.UserInLibrary;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BorrowedBooksRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BorrowedBooksRepositoryTest.class);

    private BorrowedBooksRepository borrowedBooksRepository;

    /**
     * <h2>Instancia antes do teste a configuração de banco de dados e do repositório a ser testado</h2>
     */
    @BeforeEach
    void setUp() {
        Database database = DataBaseSourceConfigTest.databaseTestSetup(new Class[]{Book.class, BorrowedBooks.class, UserInLibrary.class});
        borrowedBooksRepository = new BorrowedBooksRepository(database);
    }
}