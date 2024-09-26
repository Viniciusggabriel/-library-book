package com.library.infra.database.repositories.contracts;

import com.library.application.models.BorrowedBooks;
import com.library.infra.database.repositories.BaseRepositories;
import com.library.infra.database.repositories.finders.EntityFinder;
import com.library.util.errors.exceptions.EntityReferenceIllegal;
import com.library.util.errors.exceptions.ValueNotFound;
import io.ebean.Database;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static com.library.util.utilitarian.UpdateObjectFields.updateField;

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
                new ValueNotFound("Não foi possível encontrar o empréstimo de livro!", HttpStatus.NOT_FOUND_404)
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
            BorrowedBooks borrowedBooksUpdated = updateField(borrowedBooksInDatabase, entity);
            database.update(borrowedBooksUpdated);
        } catch (IllegalAccessException exception) {
            throw new EntityReferenceIllegal(String.format("Erro ao realizar update parcial da entidade: %s", exception.getMessage()));
        }
    }

    @Override
    public void deleteEntity(Long id) {
        BorrowedBooks borrowedBooksInDatabase = selectEntityById(id);

        database.delete(borrowedBooksInDatabase);
    }
}


