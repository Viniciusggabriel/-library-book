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

// TODO: Dar robuster ao código para que possa ter uma implementação no service
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInLibraryRepository implements BaseRepositories.UserRepository<UserInLibrary> {
    private Database database;
    private static final UserInLibraryFinder finder = new UserInLibraryFinder();

    @Override
    public UserInLibrary selectUserByID(UUID id) {
        Optional<UserInLibrary> expectedUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, id));
        expectedUserInLibrary.orElseThrow(() -> new UserInLibraryNotFound("O usuário não foi encontrado!", HttpStatus.NOT_FOUND_404));

        return expectedUserInLibrary.get();
    }

    @Override
    public String insertUser(UserInLibrary user) {
        Optional<UserInLibrary> expectedUserInLibrary = Optional.ofNullable(finder.byUserName("dsUserName", user.getDsUserName()));
        expectedUserInLibrary.ifPresent(userInLibrary -> {
            throw new ValueIsPresentInDatabase(String.format("O usuário já existe dentro do banco de dados: %s", userInLibrary.getDsUserName()), HttpStatus.NOT_FOUND_404);
        });

        database.save(user);
        return "Usuário criado com sucesso!";
    }

    @Override
    public String updateUser(UserInLibrary user, UUID id) {
        UserInLibrary userInDatabase = selectUserByID(id);

        try {
            UserInLibrary userUpdated = updateField(userInDatabase, user);
            database.update(userUpdated);
        } catch (IllegalAccessException exception) {
            throw new EntityReferenceIllegal(String.format("Erro ao realizar update parcial na entidade: %s", exception.getMessage()));
        }

        return "Usuário foi alterado com sucesso!";
    }

    @Override
    public String deleteUser(UUID id) {
        UserInLibrary userInDatabase = selectUserByID(id);

        database.delete(userInDatabase);

        return "Usuário deletado com sucesso!";
    }
}


class UserInLibraryFinder extends Finder<UUID, UserInLibrary> {

    public UserInLibraryFinder() {
        super(UserInLibrary.class);
    }

    public UserInLibrary byUserName(String atribute, String userName) {
        return query().where().eq(atribute, userName).findOne();
    }
}















