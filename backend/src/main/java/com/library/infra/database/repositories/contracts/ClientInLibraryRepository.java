package com.library.infra.database.repositories.contracts;

import com.library.application.models.ClientInLibrary;
import com.library.infra.database.repositories.BaseRepositories;
import com.library.infra.database.repositories.finders.EntityFinder;
import com.library.util.errors.exceptions.EntityAttributeAccessException;
import com.library.util.errors.exceptions.ValueAlreadyExistsException;
import com.library.util.errors.exceptions.ValueNotFoundException;
import com.library.util.utilitarian.UpdateObjectFields;
import io.ebean.Database;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientInLibraryRepository implements BaseRepositories.CrudRepository<ClientInLibrary, UUID> {
    private static final EntityFinder<UUID, ClientInLibrary> finder = new EntityFinder<>(ClientInLibrary.class);
    private final Database database;

    /**
     * <h3>Método responsável por buscar vários clientes de forma paginada dentro do banco de dados</h3>
     * <p>Utiliza a classe Finder do EBEAN para poder realizar uma consulta páginada com um numero de linhas e ordenado pelo nome do livro</p>
     *
     * @param sizeRows -> <strong>Tamanho de linhas a serem selecionados</strong>
     * @return List<Book> -> <strong>Lista com os clientes dentro do banco de dados páginados</strong>
     */
    @Override
    public List<ClientInLibrary> selectEntities(Integer sizeRows, Integer page) {
        return finder.findAll("dsClientName", sizeRows, page).getList();
    }

    /**
     * <h3>Método responsável por buscar o cliente pelo UUID</h3>
     * <p>Faz a busca do cliente com o repositório padrão do EBEAN, porem transforma em um Optional para poder ser feito um tratamento de erros melhor</p>
     *
     * @param id -> <strong>Id o cliente a ser buscado</strong>
     * @return UserInLibrary -> <strong>Cliente achado</strong>
     */
    @Override
    public ClientInLibrary selectEntityById(UUID id) {
        Optional<ClientInLibrary> expectedClientInLibrary = Optional.ofNullable(database.find(ClientInLibrary.class, id));
        expectedClientInLibrary.orElseThrow(() -> new ValueNotFoundException("O cliente não foi encontrado!", HttpStatus.NOT_FOUND_404));

        return expectedClientInLibrary.get();
    }

    /**
     * <h3>Método para inserir um cliente dentro do banco de dados</h3>
     * <p>Busca o cliente usando um método da classe Finder para realizar a busca baseado no username em formato de Optional</p>
     * <p>Caso o cliente já exista retorna erro!</p>
     *
     * @param entity -> <strong>Entidade de cliente</strong>
     */
    @Override
    public void insertEntity(ClientInLibrary entity) {
        if (entity.getDsClientName() == null || entity.getDsClientName().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente não pode ser nulo ou vazio!");
        }

        Optional<ClientInLibrary> expectedUserInLibrary = Optional.ofNullable(finder.findByName("dsClientName", entity.getDsClientName()));
        expectedUserInLibrary.ifPresent(clientIsPresent -> {
            throw new ValueAlreadyExistsException(String.format("O cliente já existe dentro do banco de dados: %s", clientIsPresent.getDsClientName()), HttpStatus.NOT_FOUND_404);
        });

        database.save(entity);
    }

    /**
     * <h3>Método para alteração parcial de um cliente</h3>
     * <p>O método usa o método de busca de cliente pelo ID e após isso joga ele juntamente aos novos dados para alteração em um método auxiliar que verifica os Fields da entidade </p>
     * <p>Após alterar o cliente ele realiza o update com os novos dados</p>
     *
     * @param entity -> <strong>Novos dados do cliente em forma de entidade</strong>
     * @param id     -> <strong>Id do cliente a ser alterado</strong>
     */
    @Override
    public void updateEntity(ClientInLibrary entity, UUID id) {
        ClientInLibrary clientInDatabase = selectEntityById(id);

        try {
            UpdateObjectFields.updatedObject(entity, clientInDatabase);
            database.update(clientInDatabase);
        } catch (IllegalAccessException exception) {
            throw new EntityAttributeAccessException(String.format("Erro ao realizar update parcial na entidade: %s", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    /**
     * <h3>Deleta um cliente</h3>
     * <p>Busca o cliente, caso ache deleta ele</p>
     *
     * @param id -> <strong>Id do cliente</strong>
     */
    @Override
    public void deleteEntity(UUID id) {
        ClientInLibrary clientInDatabase = selectEntityById(id);

        database.delete(clientInDatabase);
    }
}
