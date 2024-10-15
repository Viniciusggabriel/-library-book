package com.library.application.controllers.books;

import com.library.application.services.BookCrudService;
import com.library.util.errors.exceptions.ErrorMakingRequestException;
import com.library.util.errors.exceptions.InvalidRequestPathParameterException;
import com.library.util.errors.exceptions.MalformedJsonException;
import com.library.util.errors.exceptions.ValueNotFoundException;
import com.library.util.errors.handlers.JacksonErrorHandler;
import com.library.util.validations.validators.ValidateUrlParameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;

public class DeleteBookController extends HttpServlet {
    private final BookCrudService bookCrudService;

    public DeleteBookController() {
        this.bookCrudService = new BookCrudService();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        Long idBook = null;

        try {
            String extractIdBook = extractIdBook(req);
            idBook = ValidateUrlParameter.validateLongId(extractIdBook);
        } catch (InvalidRequestPathParameterException exception) {
            JacksonErrorHandler.handleException(exception, resp);
        }

        try {
            bookCrudService.deleteBook(idBook);
        } catch (RuntimeException exception) {
            JacksonErrorHandler.handleException(exception, resp);
        }

        resp.getWriter().write("Sucesso ao excluir livro!");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * <h3>Método privado para extrair o valor de parâmetro que está na URL da requisição</h3>
     * <p>Utiliza a classe Optional para definir se o valor vai estar ou não dentro da url, após isso tentar buscar o valor e caso não ache lança a exception</p>
     *
     * @param req -> <strong>Requisição do usuário</strong>
     * @return String -> <strong>Retorna o valor achado dentro da requisição</strong>
     * @throws InvalidRequestPathParameterException -> <strong>Exception personalizada que é lançada caso não consiga pegar o valor</strong>
     */
    private String extractIdBook(HttpServletRequest req) throws InvalidRequestPathParameterException {
        return Optional.ofNullable(req.getPathInfo())
                .map(path -> path.substring(1)) // Remove a barra inicial
                .orElseThrow(() -> new InvalidRequestPathParameterException("Não foi possível extrair o ID do livro na sua requisição!", HttpStatus.BAD_REQUEST_400));
    }
}
