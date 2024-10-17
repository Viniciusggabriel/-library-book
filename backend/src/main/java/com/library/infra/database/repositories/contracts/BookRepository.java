package com.library.infra.database.repositories.contracts;

import com.library.application.models.Book;
import com.library.infra.database.repositories.BaseRepositories;
import com.library.infra.database.repositories.finders.EntityFinder;
import com.library.util.errors.exceptions.ValueAlreadyExistsException;
import com.library.util.errors.exceptions.ValueNotFoundException;
import io.ebean.Database;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookRepository implements BaseRepositories.CrudRepository<Book, Long> {
    private final Database database;

    private static final EntityFinder<Long, Book> finder = new EntityFinder<>(Book.class);

    /**
     * <h3>Método responsável por buscar vários livros de forma paginada dentro do banco de dados</h3>
     * <p>Utiliza a classe Finder do EBEAN para poder realizar uma consulta páginada com um numero de linhas e ordenado pelo nome do livro</p>
     *
     * @param sizeRows -> <strong>Tamanho de linhas a serem selecionados</strong>
     * @return List<Book> -> <strong>Lista com os livros dentro do banco de dados páginados</strong>
     */
    @Override
    public List<Book> selectEntities(Integer sizeRows, Integer page) {
        return finder.findAll("dsBookName", sizeRows, page).getList();
    }

    /**
     * <h3>Método para buscar os livros por id</h3>
     * <p>Faz a busca do livro via ID e caso não ache lança uma exceção personalizada</p>
     *
     * @param id -> <strong>Id do livro a ser buscado</strong>
     * @return Book -> <strong>Livro encontrado pela busca</strong>
     */
    @Override
    public Book selectEntityById(Long id) {
        Optional<Book> expectedBook = Optional.ofNullable(database.find(Book.class, id));
        expectedBook.orElseThrow(() -> new ValueNotFoundException("O livro não foi encontrado!", HttpStatus.NOT_FOUND_404));

        return expectedBook.get();
    }

    /**
     * <h3>Método para inserir um livro dentro do banco de dados</h3>
     * <p>Verifica se o nome do livro é vázio, caso seja ele lança uma exceção</p>
     *
     * @param entity -> <strong>Entidade de livro a ser inserida</strong>
     */
    @Override
    public void insertEntity(Book entity) {
        if (entity.getDsBookName() == null || entity.getDsBookName().isEmpty()) {
            throw new IllegalArgumentException("O nome do livro não pode ser nulo ou vazio!");
        }

        Optional<Book> expectedBook = Optional.ofNullable(finder.findByName("dsBookName", entity.getDsBookName()));
        expectedBook.ifPresent(bookIsPresent -> {
            throw new ValueAlreadyExistsException(String.format("O livro inserido já está presente dentro do banco de dados: %s", bookIsPresent.getDsBookName()), HttpStatus.BAD_REQUEST_400);
        });

        database.save(entity);
    }

    /**
     * <h3>Método para realizar um update de forma parcial dentro do banco de dados</h3>
     * <p>Utiliza a classe Finder do EBEAN para poder realizar o update de forma correta</p>
     * <p>Chama um método static para realizar a verificação dos valores nulos da entidade usando o Fild</p>
     *
     * @param entity -> <strong>Nova entidade a ser alterada</strong>
     * @param id     -> <strong>ID do livro a ser alterado</strong>
     */
    @Override
    public void updateEntity(Book entity, Long id) {
        database.update(entity);
    }

    /**
     * <h3>Método para deletar um livro dentro do banco de dados</h3>
     *
     * @param id -> <strong>Id do livro a ser deletado</strong>
     */
    @Override
    public void deleteEntity(Long id) {
        Optional<Book> expectedBook = Optional.ofNullable(database.find(Book.class, id));
        expectedBook.ifPresent(database::delete);
        expectedBook.orElseThrow(() -> new ValueNotFoundException("O livro a ser deletado não encontrado!", HttpStatus.BAD_REQUEST_400));
    }
}