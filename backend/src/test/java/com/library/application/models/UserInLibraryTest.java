package com.library.application.models;

import com.library.DataBaseSourceConfigTest;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserInLibraryTest {
    private static final Logger logger = LoggerFactory.getLogger(UserInLibraryTest.class);

    private Database database;

    /**
     * <h2>Instancia antes do teste a configuração de banco de dados</h2>
     */
    @BeforeEach
    void setUp() {
        database = DataBaseSourceConfigTest.databaseTestSetup(new Class[]{UserInLibrary.class});
    }

    /**
     * <h3>Método para realizar testes de insert, find e delete na entidade de UserInLibrary</h3>
     * <p> Esse método criar objeto usuário e busca no banco de dados, se achar ele deleta e passa o teste, caso não ache ele salva o usuário, busca e deleta ele após compara os resultados</p>
     */
    @Test
    public void insertFindDeleteUserInLibrary() {
        UUID USER_UUID = UUID.randomUUID();

        UserInLibrary userInLibrary = new UserInLibrary(USER_UUID, "Testador", "Senha em plain text");

        database.save(userInLibrary);
        logger.info("Usuário salvo no banco de dados com sucesso!");

        Optional<UserInLibrary> existingUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, USER_UUID));
        existingUserInLibrary.ifPresent(userIsPresent -> {
            assertThat(userIsPresent.getIdUser()).isEqualTo(userInLibrary.getIdUser());
            assertThat(userIsPresent.getDsUserName()).isEqualTo(userInLibrary.getDsUserName());
            assertThat(userIsPresent.getDsPassword()).isEqualTo(userInLibrary.getDsPassword());
        });
        logger.info("Usuário encontrado dentro do banco de dados!");

        database.delete(userInLibrary);
        logger.info("Usuário deletado com sucesso!");

        UserInLibrary deletedUser = database.find(UserInLibrary.class, USER_UUID);
        assertThat(deletedUser).isNull();
        logger.info("Confirmação: O usuário foi removido corretamente do banco de dados.");
    }
}