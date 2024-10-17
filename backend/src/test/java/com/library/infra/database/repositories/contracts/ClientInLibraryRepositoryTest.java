package com.library.infra.database.repositories.contracts;

import com.library.DataBaseSourceConfigTest;
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

class ClientInLibraryRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientInLibraryRepositoryTest.class);

    private ClientInLibraryRepository clientInLibraryRepository;
    private UserInLibrary userInLibrary;
    private Database database;

    /**
     * <h2>Instancia antes do teste a configuração de banco de dados</h2>
     */
    @BeforeEach
    void setUp() {
        database = DataBaseSourceConfigTest.databaseTestSetup(new Class[]{ClientInLibrary.class, UserInLibrary.class});
        clientInLibraryRepository = new ClientInLibraryRepository(database);
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
            logger.debug("O cliente já existia no banco de dados: {}", userIsPresent.getDsUserName());
        });

        // Caso não exista cria
        database.save(userInLibrary);
        logger.info("Usuário salvo novamente no banco de dados com sucesso!");
    }

    /**
     * <h3>Teste para verificar se o repositório está inserindo, alterando, buscando e deletando o cliente de forma correta</h3>
     * <p>O cliente é instanciado e salvo via repositório, após isso o seu username é alterado e só assim a busca é feita já visando verificar se o nome foi trocado compara o nome trocado com o achado, após isso o cliente é deletado e buscado novamente esperando receber uma exception personalizada</p>
     */
    @Test
    public void insertUpdateFindDeleteClientInLibrary() {
        UUID CLIENT_UUID = UUID.randomUUID();
        ClientInLibrary clientInLibrary = new ClientInLibrary(
                CLIENT_UUID,
                "TestadorCliente",
                "62987654321",
                "testador@email.com",
                ZonedDateTime.now().withNano(0),
                userInLibrary
        );
        logger.info("O objeto de cliente foi criado!");

        clientInLibraryRepository.insertEntity(clientInLibrary);
        logger.info("O cliente foi salvo com sucesso!");

        ClientInLibrary userForUpdate = new ClientInLibrary();
        userForUpdate.setDsClientName("Testador alterado");

        clientInLibraryRepository.updateEntity(userForUpdate, CLIENT_UUID);
        logger.info("O cliente foi alterado com sucesso!");

        Optional<ClientInLibrary> client = Optional.ofNullable(clientInLibraryRepository.selectEntityById(CLIENT_UUID));
        client.ifPresent(clientIsPresent -> {
            assertThat(clientIsPresent.getIdClient()).isEqualTo(clientInLibrary.getIdClient());
            assertThat(clientIsPresent.getDsClientName()).isEqualTo(userForUpdate.getDsClientName());
            assertThat(clientIsPresent.getDsPhoneNumber()).isEqualTo(clientInLibrary.getDsPhoneNumber());
            assertThat(clientIsPresent.getDsEmail()).isEqualTo(clientInLibrary.getDsEmail());
            assertThat(clientIsPresent.getFkIdUserInLibrary()).isEqualTo(clientInLibrary.getFkIdUserInLibrary());
            assertThat(clientIsPresent.getDsDateOfBirth()).isEqualTo(clientInLibrary.getDsDateOfBirth());
        });
        logger.info("O cliente foi achado com sucesso pelo seu ID!");

        clientInLibraryRepository.deleteEntity(CLIENT_UUID);
        logger.info("O cliente foi deletado com sucesso!");

        assertThatThrownBy(() -> {
            clientInLibraryRepository.selectEntityById(CLIENT_UUID);
            logger.info("Confirmação: O cliente foi removido corretamente do banco de dados.");
        })
                .isInstanceOf(ValueNotFoundException.class)
                .hasMessageContaining("O cliente não foi encontrado!");
    }

    /**
     * <h3>Teste para verificar se a busca de vários itens dentro do banco de dados está sendo executada com sucesso</h3>
     */
    @Test
    public void selectAllBooks() {
        List<ClientInLibrary> books = new ArrayList<>();

        ClientInLibrary book;
        for (int value = 1; value <= 9; value++) {
            book = new ClientInLibrary();
            book.setIdClient(UUID.randomUUID());
            book.setDsClientName("Testador " + value);
            book.setDsEmail("Testador" + value + "@email.com");
            book.setDsPhoneNumber("6298765432" + value);
            book.setDsDateOfBirth(ZonedDateTime.now().withNano(0));
            book.setFkIdUserInLibrary(userInLibrary);

            books.add(book);
        }

        // Insere os clientes gerados pelo for
        books.forEach(clientInArray -> {
            clientInLibraryRepository.insertEntity(clientInArray);
        });

        List<ClientInLibrary> clientsList = clientInLibraryRepository.selectEntities(6,1);
        clientsList.forEach(bookInArray -> logger.info("Clientes foram listados de forma correta: {}", bookInArray));
    }
}