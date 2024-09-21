package com.library.application.models;

import com.library.application.StartDatabaseTest;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserInLibraryTest {
    private static final Logger logger = LoggerFactory.getLogger(UserInLibraryTest.class);

    private Database database;

    @BeforeEach
    void setUp() {
        database = StartDatabaseTest.databaseTestSetup();
    }

    /**
     * Método para realizar testes de insert, find e delete na entidade de UserInLibrary
     * <p>
     * Esse método criar objeto usuário e busca no banco de dados, se achar ele deleta e passa o teste, caso não ache ele salva o usuário, busca e deleta ele após compara os resultados
     * </p>
     */
    @Test
    public void insertFindDeleteUserInLibrary() {
        UUID userUUID = UUID.randomUUID();

        UserInLibrary userInLibrary = new UserInLibrary();
        userInLibrary.setIdUser(userUUID);
        userInLibrary.setDsUserName("Testador");
        userInLibrary.setDsPassword("Senha em plain text");

        // Limpa o usuário se já existe
        if (Objects.equals(database.find(UserInLibrary.class, userUUID), userInLibrary)) {
            database.delete(userInLibrary);
            logger.debug("O usuário já existia no banco de dados, ele foi apagado!");
        }

        database.save(userInLibrary);
        logger.info("Usuário salvo no banco de dados com sucesso!");

        UserInLibrary foundUserInLibrary = database.find(UserInLibrary.class, userUUID);
        logger.info("Usuário encontrado dentro do banco de dados!");

        assertThat(foundUserInLibrary.getIdUser()).isEqualTo(userInLibrary.getIdUser());
        assertThat(foundUserInLibrary.getDsUserName()).isEqualTo(userInLibrary.getDsUserName());
        assertThat(foundUserInLibrary.getDsPassword()).isEqualTo(userInLibrary.getDsPassword());

        database.delete(userInLibrary);
        logger.info("Usuário deletado com sucesso!");
    }
}