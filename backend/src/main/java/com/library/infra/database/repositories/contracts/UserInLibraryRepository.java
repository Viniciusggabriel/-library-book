package com.library.infra.database.repositories.contracts;

import com.library.application.models.UserInLibrary;
import com.library.infra.database.repositories.BaseRepositories;
import com.library.util.errors.exceptions.EntityReferenceIllegal;
import com.library.util.errors.exceptions.UserInLibraryNotFound;
import com.library.util.errors.exceptions.ValueIsPresentInDatabase;
import io.ebean.Database;
import io.ebean.Finder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static com.library.util.utilitarian.UpdateObjectFields.updateField;

@AllArgsConstructor
@RequiredArgsConstructor
public class UserInLibraryRepository implements BaseRepositories.UserRepository<UserInLibrary> {
    private Database database;

    private static final UserInLibraryFinder finder = new UserInLibraryFinder();

    /**
     * <h3>Método responsável por buscar o usuário pelo UUID</h3>
     * <p>Faz a busca do usuário com o repositório padrão do EBEAN, porem transforma em um Optional para poder ser feito um tratamento de erros melhor</p>
     *
     * @param id -> <strong>Id o usuário a ser buscado</strong>
     * @return UserInLibrary -> <strong>Usuário achado</strong>
     */
    @Override
    public UserInLibrary selectUserByID(UUID id) {
        Optional<UserInLibrary> expectedUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, id));
        expectedUserInLibrary.orElseThrow(() -> new UserInLibraryNotFound("O usuário não foi encontrado!", HttpStatus.NOT_FOUND_404));

        return expectedUserInLibrary.get();
    }

    /**
     * <h3>Método para inserir um usuário dentro do banco de dados</h3>
     * <p>Busca o usuário usando um método da classe Finder para realizar a busca baseado no username em formato de Optional</p>
     * <p>Caso o usuário já exista retorna erro!</p>
     *
     * @param user -> <strong>Entidade de usuário</strong>
     */
    @Override
    public void insertUser(UserInLibrary user) {
        Optional<UserInLibrary> expectedUserInLibrary = Optional.ofNullable(finder.byUserName("dsUserName", user.getDsUserName()));
        expectedUserInLibrary.ifPresent(userInLibrary -> {
            throw new ValueIsPresentInDatabase(String.format("O usuário já existe dentro do banco de dados: %s", userInLibrary.getDsUserName()), HttpStatus.NOT_FOUND_404);
        });

        database.save(user);
    }

    /**
     * <h3>Método para alteração parcial de um usuário</h3>
     * <p>O método usa o método de busca de usuário pelo ID e após isso joga ele juntamente aos novos dados para alteração em um método auxiliar que verifica os Fields da entidade </p>
     * <p>Após alterar o usuário ele realiza o update com os novos dados</p>
     *
     * @param user -> <strong>Novos dados do usuário em forma de entidade</strong>
     * @param id   -> <strong>Id do usuário a ser alterado</strong>
     */
    @Override
    public void updateUser(UserInLibrary user, UUID id) {
        UserInLibrary userInDatabase = selectUserByID(id);

        try {
            UserInLibrary userUpdated = updateField(userInDatabase, user);
            database.update(userUpdated);
        } catch (IllegalAccessException exception) {
            throw new EntityReferenceIllegal(String.format("Erro ao realizar update parcial na entidade: %s", exception.getMessage()));
        }
    }

    /**
     * <h3>Deleta um usuário</h3>
     * <p>Busca o usuário, caso ache deleta ele</p>
     *
     * @param id -> <strong>Id do usuário</strong>
     */
    @Override
    public void deleteUser(UUID id) {
        UserInLibrary userInDatabase = selectUserByID(id);

        database.delete(userInDatabase);
    }
}


class UserInLibraryFinder extends Finder<UUID, UserInLibrary> {

    public UserInLibraryFinder() {
        super(UserInLibrary.class);
    }

    /**
     * <h3>Método para buscar usuários pelo username</h3>
     *
     * @param atribute -> <strong>Atributo dentro da entidade</strong>
     * @param userName -> <strong>Username do usuário a ser deletado</strong>
     * @return UserInLibrary -> <strong>Usuário encontrado</strong>
     */
    public UserInLibrary byUserName(String atribute, String userName) {
        return query().where().eq(atribute, userName).findOne();
    }
}















