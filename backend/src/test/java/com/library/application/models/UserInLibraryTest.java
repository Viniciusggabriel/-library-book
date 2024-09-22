package com.library.application.models;

import com.library.application.DataBaseSourceConfigTest;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserInLibraryTest {
    private static final Logger logger = LoggerFactory.getLogger(UserInLibraryTest.class);

    private Database database;

    @BeforeEach
    void setUp() {
        database = DataBaseSourceConfigTest.databaseTestSetup(List.of(UserInLibrary.class));
    }

    /**
     * Método para realizar testes de insert, find e delete na entidade de UserInLibrary
     * <p>
     * Esse método criar objeto usuário e busca no banco de dados, se achar ele deleta e passa o teste, caso não ache ele salva o usuário, busca e deleta ele após compara os resultados
     * </p>
     */
    @Test
    public void insertFindDeleteUserInLibrary() {
        UUID USER_UUID = UUID.randomUUID();

        UserInLibrary userInLibrary = new UserInLibrary();
        userInLibrary.setIdUser(USER_UUID);
        userInLibrary.setDsUserName("Testador");
        userInLibrary.setDsPassword("Senha em plain text");

        database.save(userInLibrary);
        logger.info("Usuário salvo no banco de dados com sucesso!");

        Optional<UserInLibrary> existingUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, USER_UUID));
        existingUserInLibrary.ifPresent(borrowedBooksIsPresent -> {
            assertThat(borrowedBooksIsPresent.getIdUser()).isEqualTo(userInLibrary.getIdUser());
            assertThat(borrowedBooksIsPresent.getDsUserName()).isEqualTo(userInLibrary.getDsUserName());
            assertThat(borrowedBooksIsPresent.getDsPassword()).isEqualTo(userInLibrary.getDsPassword());
        });
        logger.info("Usuário encontrado dentro do banco de dados!");

        database.delete(userInLibrary);
        logger.info("Usuário deletado com sucesso!");

        UserInLibrary deletedBook = database.find(UserInLibrary.class, USER_UUID);
        assertThat(deletedBook).isNull();
        logger.info("Confirmação: O usuário foi removido corretamente do banco de dados.");
    }
}