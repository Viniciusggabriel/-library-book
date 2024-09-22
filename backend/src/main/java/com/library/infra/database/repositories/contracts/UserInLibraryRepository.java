package com.library.infra.database.repositories.contracts;

import com.library.application.models.UserInLibrary;
import com.library.infra.database.repositories.DefaultMethods;
import com.library.util.errors.exceptions.UserInLibraryNotFound;
import com.library.util.errors.exceptions.ValueIsPresentInDatabase;
import com.library.util.utilitarian.UpdateObjectFields;
import io.ebean.Database;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

// TODO: Dar robuster ao código para que possa ter uma implementação no service
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInLibraryRepository implements DefaultMethods.UserRepository<UserInLibrary> {
    private static Database database;

    @Override
    public UserInLibrary selectUserByID(UUID id) {
        Optional<UserInLibrary> expectedUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, id));
        expectedUserInLibrary.orElseThrow(() -> new UserInLibraryNotFound("O usuário não foi encontrado!", HttpStatus.NOT_FOUND_404));

        return expectedUserInLibrary.get();
    }

    @Override
    public String insertUser(UserInLibrary user) {
        Optional<UserInLibrary> expectedUserInLibrary = Optional.ofNullable(database.find(UserInLibrary.class, user.getDsUserName()));
        expectedUserInLibrary.ifPresent(userInLibrary -> {
            throw new ValueIsPresentInDatabase(String.format("O usuário já existe dentro do banco de dados: %s", userInLibrary.getDsUserName()), HttpStatus.NOT_FOUND_404);
        });

        database.save(user);
        return "Usuário criado com sucesso!";
    }

    @Override
    public String updateUser(UserInLibrary user, UUID id) throws IllegalAccessException {
        UserInLibrary userInDatabase = selectUserByID(id);

        UpdateObjectFields.updateField(userInDatabase, user);
        database.update(userInDatabase);

        return "Usuário foi alterado com sucesso!";
    }

    @Override
    public String deleteUser(UUID id) {
        UserInLibrary userInDatabase = selectUserByID(id);

        database.delete(userInDatabase);

        return "Usuário deletado com sucesso!";
    }
}
