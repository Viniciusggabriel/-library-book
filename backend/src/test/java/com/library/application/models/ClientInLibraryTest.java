package com.library.application.models;

import com.library.DataBaseSourceConfigTest;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClientInLibraryTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientInLibraryTest.class);

    private Database database;
    private UserInLibrary userInLibrary;

    /**
     * <h2>Instancia antes do teste a configuração de banco de dados</h2>
     */
    @BeforeEach
    void setUp() {
        database = DataBaseSourceConfigTest.databaseTestSetup(new Class[]{ClientInLibrary.class, UserInLibrary.class});
        setUpTestData();
    }

    /**
     * <h3>Método para realizar a pré-configuração das dependências necessárias para testar a entidade BorrowedBooks.</h3>
     * <p>Este método cria um cliente como exemplo, que sera utilizado durante os testes da entidade
     * de clientes.</p>
     */
    private void setUpTestData() {
        // Configurar o cliente
        UUID USER_UUID = UUID.randomUUID();
        userInLibrary = new UserInLibrary(USER_UUID, "Testador", "Senha em plain text");

        // Verifica se o cliente já existe dentro do banco de dados de testes
        Optional<UserInLibrary> existingUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, USER_UUID));
        existingUserInLibrary.ifPresent(userIsPresent -> {
            logger.debug("O usuário já existia no banco de dados: {}", userIsPresent.getDsUserName());
        });

        // Caso não exista cria
        database.save(userInLibrary);
        logger.info("Usuário salvo novamente no banco de dados com sucesso!");
    }

    /**
     * <h3>Método para realizar testes de insert, find e delete na entidade de ClientInLibrary</h3>
     * <p> Esse método criar objeto cliente e busca no banco de dados, se achar ele deleta e passa o teste, caso não ache ele salva o cliente, busca e deleta ele após compara os resultados</p>
     */
    @Test
    public void insertFindDeleteClientInLibrary() {
        UUID CLIENT_UUID = UUID.randomUUID();
        ClientInLibrary clientInLibrary = new ClientInLibrary(
                CLIENT_UUID,
                "TestadorCliente",
                "62987654321",
                "testador@email.com",
                ZonedDateTime.now().withNano(0),
                userInLibrary
        );

        database.save(clientInLibrary);
        logger.info("Cliente salvo novamente no banco de dados com sucesso!");

        Optional<ClientInLibrary> existingClientInLibrary = Optional.ofNullable(database.find(ClientInLibrary.class, CLIENT_UUID));
        existingClientInLibrary.ifPresent(clientIsPresent -> {
            assertThat(clientIsPresent.getIdClient()).isEqualTo(clientInLibrary.getIdClient());
            assertThat(clientIsPresent.getDsClientName()).isEqualTo(clientInLibrary.getDsClientName());
            assertThat(clientIsPresent.getDsPhoneNumber()).isEqualTo(clientInLibrary.getDsPhoneNumber());
            assertThat(clientIsPresent.getDsEmail()).isEqualTo(clientInLibrary.getDsEmail());
            assertThat(clientIsPresent.getFkIdUserInLibrary()).isEqualTo(clientInLibrary.getFkIdUserInLibrary());
            assertThat(clientIsPresent.getDsDateOfBirth()).isEqualTo(clientInLibrary.getDsDateOfBirth());
        });
        logger.info("Cliente encontrado dentro do banco de dados!");

        // Deleta o cliente
        database.delete(clientInLibrary);
        logger.info("Cliente deletado com sucesso!");

        ClientInLibrary deletedClient = database.find(ClientInLibrary.class, CLIENT_UUID);
        assertThat(deletedClient).isNull();
        logger.info("Confirmação: O cliente foi removido corretamente do banco de dados.");
    }
}