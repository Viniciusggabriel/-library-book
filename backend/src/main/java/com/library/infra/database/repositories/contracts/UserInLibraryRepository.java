package com.library.infra.database.repositories.contracts;

import com.library.application.models.UserInLibrary;
import com.library.infra.database.repositories.DefaultMethods;
import io.ebean.Database;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

// TODO: Desenvolver esse repositório
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInLibraryRepository implements DefaultMethods.UserRepository<UserInLibrary> {
    private static Database database;


    @Override
    public UserInLibrary selectUserByID(UUID id) {
        return database.find(UserInLibrary.class, id);
    }

    @Override
    public String insertUser(UserInLibrary user) {
        database.save(user);
        return "";
    }

    @Override
    public String updateUser(UserInLibrary user, UUID id) {
        database.update(UserInLibrary.class);
        return "";
    }

    @Override
    public String deleteUser(UUID id) {
        return "";
    }
}
