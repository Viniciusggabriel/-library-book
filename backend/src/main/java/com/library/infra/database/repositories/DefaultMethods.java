package com.library.infra.database.repositories;

import java.util.List;
import java.util.UUID;

public interface DefaultMethods {
    interface CrudRepository<T, ID> {
        List<T> selectEntities();

        T selectEntityById(ID id);

        String insertEntity(T entity);

        String updateEntity(T entity, ID id);

        String deleteEntity(ID id);
    }

    interface UserRepository<T> {
        T selectUserByID(UUID id);

        String insertUser(T user);

        String updateUser(T user, UUID id);

        String deleteUser(UUID id);
    }
}
