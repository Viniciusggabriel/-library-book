package com.library.application.services;

import com.library.application.dto.requests.BookRequest;
import com.library.application.dto.responses.BookResponse;
import com.library.application.models.Book;
import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.database.repositories.contracts.BookRepository;
import com.library.util.errors.exceptions.MalformedJsonException;
import com.library.util.utilitarian.ManipulateJsonObject;
import com.library.util.utilitarian.UpdateObjectFields;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookCrudService {
    private final BookRepository bookRepository;
    private Book bookRequest;

    public BookCrudService() {
        this.bookRepository = new BookRepository(DataBaseSourceConfig.getDatabase());
    }

    /**
     * <h3>Método de service para buscar um livro baseado nos ID</h3>
     *
     * @param idBook -> <strong>Id do livro a ser buscado</strong>
     * @return char[] -> <strong>Json com o livro encontrado</strong>
     * @throws MalformedJsonException -> <strong>Exception para json com erros de formação</strong>
     */
    public char[] getBookById(Long idBook) throws MalformedJsonException {
        Book bookInDatabase = bookRepository.selectEntityById(idBook);

        BookResponse bookResponse = BookResponse.of(
                bookInDatabase.getIdBook(),
                bookInDatabase.getDsQuantityBooks(),
                bookInDatabase.getDsBookName(),
                bookInDatabase.getDsAuthorName(),
                bookInDatabase.getDsReleaseDate(),
                bookInDatabase.getDsSummary()
        );

        return ManipulateJsonObject.readJson(bookResponse);
    }

    /**
     * <h3>Método para inserir um livro dentro do banco de dados</h3>
     *
     * @param bookRequest -> <strong>DOT obtido do usuário via payload http</strong>
     */
    public void postBook(BookRequest bookRequest) {
        this.bookRequest = new Book();

        this.bookRequest.setDsBookName(bookRequest.dsBookName());
        this.bookRequest.setDsAuthorName(bookRequest.dsAuthorName());
        this.bookRequest.setDsReleaseDate(bookRequest.dsReleaseDate());
        this.bookRequest.setDsSummary(bookRequest.dsSummary());
        this.bookRequest.setDsQuantityBooks(bookRequest.dsQuantityBooks());

        bookRepository.insertEntity(this.bookRequest);
    }

    /**
     * <h3>Método para realizar patch em um livro dentro do banco de dados</h3>
     * <p>O método instancia um novo objeto de entidade e seta os valores obtidos do payload via controller, após isso executa o método de update do repósitorio, passando a entiade e o ID do livro, após isso chama o método da mesma classe para buscar o livro e retornar ele alterado para o usuário</p>
     *
     * @param bookRequest -> <strong>DTO obtido pelo controller para busca de dados</strong>
     * @param idBook      -> <strong>Id do livro a ser buscado</strong>
     * @return char[] -> <strong>Json montado pelo método que busca livros</strong>
     * @throws MalformedJsonException -> <strong>Exception personalizada para json mal formado </strong>
     */
    public char[] putBook(BookRequest bookRequest, Long idBook) throws MalformedJsonException, IllegalAccessException {
        Book bookInDatabase = bookRepository.selectEntityById(idBook);

        this.bookRequest = new Book();
        this.bookRequest.setDsBookName(bookRequest.dsBookName());
        this.bookRequest.setDsAuthorName(bookRequest.dsAuthorName());
        this.bookRequest.setDsReleaseDate(bookRequest.dsReleaseDate());
        this.bookRequest.setDsSummary(bookRequest.dsSummary());
        this.bookRequest.setDsQuantityBooks(bookRequest.dsQuantityBooks());

        UpdateObjectFields.updatedObject(bookInDatabase, this.bookRequest);

        bookRepository.updateEntity(bookInDatabase, idBook);
        return getBookById(idBook);
    }
}
