package com.library.infra.database.repositories;

import com.library.application.models.Book;

import java.util.List;
import java.util.UUID;

/**
 * <h1>Interface principal para métodos dos repositórios</h1>
 */
public interface BaseRepositories {

    interface CrudRepository<T, ID> {
        default List<T> selectEntities(Integer sizeRows, Integer page) {
            return List.of();
        }

        default T selectEntityById(ID id) {
            return null;
        }

        default void insertEntity(T entity) {
        }

        default void updateEntity(T entity, ID id) throws IllegalAccessException {
        }

        default void deleteEntity(ID id) {
        }
    }

    interface UserRepository<T> {
        default T selectUserByID(UUID id) {
            return null;
        }

        default void insertUser(T user) {
        }

        default void updateUser(T user, UUID id) throws IllegalAccessException {
        }

        default void deleteUser(UUID id) {
        }
    }
}
