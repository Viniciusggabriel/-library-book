package com.library.infra.database.repositories;

import java.util.List;
import java.util.UUID;

/**
 * <h1>Interface principal para métodos dos repositórios</h1>
 */
public interface BaseRepositories {
    interface CrudRepository<T, ID> {
        List<T> selectEntities(Integer sizeRows);

        T selectEntityById(ID id);

        void insertEntity(T entity);

        void updateEntity(T entity, ID id);

        void deleteEntity(ID id);
    }

    interface UserRepository<T> {
        T selectUserByID(UUID id);

        void insertUser(T user);

        void updateUser(T user, UUID id) throws IllegalAccessException;

        void deleteUser(UUID id);
    }
}
