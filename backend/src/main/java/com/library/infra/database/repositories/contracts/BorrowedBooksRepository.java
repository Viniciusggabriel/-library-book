package com.library.infra.database.repositories.contracts;

import com.library.application.models.BorrowedBooks;
import com.library.infra.database.repositories.BaseRepositories;
import com.library.infra.database.repositories.finders.EntityFinder;
import com.library.util.errors.exceptions.EntityAttributeAccessException;
import com.library.util.errors.exceptions.ValueNotFoundException;
import com.library.util.utilitarian.UpdateObjectFields;
import io.ebean.Database;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class BorrowedBooksRepository implements BaseRepositories.CrudRepository<BorrowedBooks, Long> {
    private final Database database;

    private static final EntityFinder<Long, BorrowedBooks> finder = new EntityFinder<>(BorrowedBooks.class);

    @Override
    public List<BorrowedBooks> selectEntities(Integer sizeRows) {
        return finder.findAll("idBorrowed", sizeRows).getList();
    }

    @Override
    public BorrowedBooks selectEntityById(Long id) {
        Optional<BorrowedBooks> expectedBorrowedBooks = Optional.ofNullable(database.find(BorrowedBooks.class, id));
        expectedBorrowedBooks.orElseThrow(() ->
                new ValueNotFoundException("Não foi possível encontrar o empréstimo de livro!", HttpStatus.NOT_FOUND_404)
        );

        return expectedBorrowedBooks.get();
    }

    @Override
    public void insertEntity(BorrowedBooks entity) {
        database.insert(entity);
    }

    @Override
    public void updateEntity(BorrowedBooks entity, Long id) {
        BorrowedBooks borrowedBooksInDatabase = selectEntityById(id);

        try {
            UpdateObjectFields.updatedObject(entity, borrowedBooksInDatabase);
            database.update(borrowedBooksInDatabase);
        } catch (IllegalAccessException exception) {
            throw new EntityAttributeAccessException(String.format("Erro ao realizar update parcial da entidade: %s", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public void deleteEntity(Long id) {
        BorrowedBooks borrowedBooksInDatabase = selectEntityById(id);

        database.delete(borrowedBooksInDatabase);
    }
}


