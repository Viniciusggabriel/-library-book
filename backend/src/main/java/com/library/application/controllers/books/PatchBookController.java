package com.library.application.controllers.books;

import com.library.application.dto.requests.BookRequest;
import com.library.application.services.BookCrudService;
import com.library.util.errors.exceptions.InvalidRequestPathParameterException;
import com.library.util.utilitarian.ManipulateJsonObject;
import com.library.util.validations.validators.ValidateUrlParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class PatchBookController extends HttpServlet {
    private final BookCrudService bookCrudService;

    public PatchBookController() {
        this.bookCrudService = new BookCrudService();
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String extractIdBook;
        Long idBook;

        // Estrai o ID do livro na url e valida
        try {
            extractIdBook = extractIdBook(req);
            idBook = ValidateUrlParameter.validateLongId(extractIdBook);
        } catch (InvalidRequestPathParameterException exception) {
            throw new InvalidRequestPathParameterException(exception.getMessage(), exception.getHttpStatus(), exception.getCause());
        }

        // Lê o payload da requisição
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String lineJson;
        while ((lineJson = reader.readLine()) != null) {
            stringBuilder.append(lineJson);
        }

        BookRequest bookRequest = ManipulateJsonObject.generateJson(stringBuilder, BookRequest.class);
        bookCrudService.postBook(bookRequest);

        char[] jsonBook;
        try {
            // Chama o service para realizar o patch
            jsonBook = bookCrudService.patchBook(bookRequest, idBook);

            if (jsonBook == null) throw new ServletException("Erro ao montar json com o livro!");
        } catch (Exception exception) {
            throw new InvalidRequestPathParameterException(exception.getMessage(), HttpStatus.BAD_GATEWAY_502, exception.getCause());
        }

        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(jsonBook);
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
        return Optional.ofNullable(req.getPathInfo()).map(path -> path.substring(1)) // Remove a barra inicial
                .orElseThrow(() -> new InvalidRequestPathParameterException("Não foi possível extrair o ID do livro na sua requisição!", HttpStatus.BAD_REQUEST_400));
    }
}
