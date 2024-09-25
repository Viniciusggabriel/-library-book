package com.library.infra.database.repositories.contracts;

import com.library.DataBaseSourceConfigTest;
import com.library.application.models.UserInLibrary;
import com.library.util.errors.exceptions.ValueNotFound;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserInLibraryRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(UserInLibraryRepositoryTest.class);

    private UserInLibraryRepository userInLibraryRepository;

    /**
     * <h2>Instancia antes do teste a configuração de banco de dados e do repositório a ser testado</h2>
     */
    @BeforeEach
    void setUp() {
        Database database = DataBaseSourceConfigTest.databaseTestSetup(new Class[]{UserInLibrary.class});
        userInLibraryRepository = new UserInLibraryRepository(database);
    }

    /**
     * <h3>Teste para verificar se o repositório está inserindo, alterando, buscando e deletando o usuário de forma correta</h3>
     * <p>O usuário é instanciado e salvo via repositório, após isso o seu username é alterado e só assim a busca é feita já visando verificar se o nome foi trocado compara o nome trocado com o achado, após isso o usuário é deletado e buscado novamente esperando receber uma exception personalizada</p>
     */
    @Test
    public void insertUpdateFindDeleteUserInLibrary() {
        UUID USER_UUID = UUID.randomUUID();
        UserInLibrary userInLibrary = new UserInLibrary(USER_UUID, "Testador", "Senha em plain text");
        logger.info("O objeto de usuário foi criado!");

        userInLibraryRepository.insertUser(userInLibrary);
        logger.info("O usuário foi salvo com sucesso!");

        UserInLibrary userForUpdate = new UserInLibrary();
        userForUpdate.setDsUserName("Testador alterado");

        userInLibraryRepository.updateUser(userForUpdate, USER_UUID);
        logger.info("O usuário foi alterado com sucesso!");

        Optional<UserInLibrary> user = Optional.ofNullable(userInLibraryRepository.selectUserByID(USER_UUID));
        user.ifPresent(userIsPresent -> {
            assertThat(userIsPresent.getIdUser()).isEqualTo(userInLibrary.getIdUser());
            assertThat(userIsPresent.getDsUserName()).isEqualTo(userForUpdate.getDsUserName());
            assertThat(userIsPresent.getDsPassword()).isEqualTo(userInLibrary.getDsPassword());
        });
        logger.info("O usuário foi achado com sucesso pelo seu ID!");

        userInLibraryRepository.deleteUser(USER_UUID);
        logger.info("O usuário foi deletado com sucesso!");

        assertThatThrownBy(() -> {
            userInLibraryRepository.selectUserByID(USER_UUID);
            logger.info("Confirmação: O usuário foi removido corretamente do banco de dados.");
        })
                .isInstanceOf(ValueNotFound.class)
                .hasMessageContaining("O usuário não foi encontrado!")
                .hasMessageContaining("status: 404");
    }
}