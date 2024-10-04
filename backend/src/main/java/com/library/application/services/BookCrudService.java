package com.library.application.services;

import com.library.application.dto.requests.BookRequest;
import com.library.application.dto.responses.BookResponse;
import com.library.application.models.Book;
import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.database.repositories.contracts.BookRepository;
import com.library.util.errors.exceptions.MalformedJsonException;
import com.library.util.utilitarian.ManipulateJsonObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookCrudService {
    private final BookRepository bookRepository;
    private Book book;

    public BookCrudService() {
        this.bookRepository = new BookRepository(DataBaseSourceConfig.getDatabase());
    }

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

    public void postBook(BookRequest bookRequest) {
        book = new Book();

        book.setDsBookName(bookRequest.dsBookName());
        book.setDsAuthorName(bookRequest.dsAuthorName());
        book.setDsReleaseDate(bookRequest.dsReleaseDate());
        book.setDsSummary(bookRequest.dsSummary());
        book.setDsQuantityBooks(bookRequest.dsQuantityBooks());

        bookRepository.insertEntity(book);
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
    public char[] patchBook(BookRequest bookRequest, Long idBook) throws MalformedJsonException {
        book = new Book();

        book.setDsBookName(bookRequest.dsBookName());
        book.setDsAuthorName(bookRequest.dsAuthorName());
        book.setDsReleaseDate(bookRequest.dsReleaseDate());
        book.setDsSummary(bookRequest.dsSummary());
        book.setDsQuantityBooks(bookRequest.dsQuantityBooks());

        bookRepository.updateEntity(book, idBook);

        return this.getBookById(idBook);
    }
}
