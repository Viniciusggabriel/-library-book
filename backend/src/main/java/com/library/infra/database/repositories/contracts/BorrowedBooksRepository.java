package com.library.infra.database.repositories.contracts;

import com.library.application.models.BorrowedBooks;
import com.library.infra.database.repositories.BaseRepositories;
import com.library.infra.database.repositories.finders.EntityFinder;
import io.ebean.Database;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BorrowedBooksRepository implements BaseRepositories.CrudRepository<BorrowedBooks, Long> {
    private final Database database;

    private static final EntityFinder<Long, BorrowedBooks> finder = new EntityFinder<>(BorrowedBooks.class);

    @Override
    public List<BorrowedBooks> selectEntities(Integer sizeRows) {
        return List.of();
    }

    @Override
    public BorrowedBooks selectEntityById(Long aLong) {
        return null;
    }

    @Override
    public void insertEntity(BorrowedBooks entity) {

    }

    @Override
    public void updateEntity(BorrowedBooks entity, Long aLong) {

    }

    @Override
    public void deleteEntity(Long aLong) {

    }
}


