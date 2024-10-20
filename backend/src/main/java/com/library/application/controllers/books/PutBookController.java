package com.library.application.controllers.books;

import com.library.application.dto.requests.BookRequest;
import com.library.application.services.BookCrudService;
import com.library.util.errors.exceptions.InputOutputDataException;
import com.library.util.errors.exceptions.InvalidRequestPathParameterException;
import com.library.util.errors.exceptions.MalformedJsonException;
import com.library.util.errors.handlers.JacksonErrorHandler;
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
public class PutBookController extends HttpServlet {
    private final BookCrudService bookCrudService;

    public PutBookController() {
        this.bookCrudService = new BookCrudService();
    }

    /**
     * <h3>Método privado para gerar a string com o payload da requisição</h3>
     * <p>Recebe a requisição http e monta um StringBuilder para poder realizar um buffer com os dados obtidos </p>
     *
     * @param req -> <strong>Requisição http</strong>
     * @return StringBuilder -> <strong>String buffer com os dados recebidos da requisição</strong>
     * @throws InputOutputDataException -> <strong>Exception para erros ao processar dados do json</strong>
     */
    private static StringBuilder getStringBuilder(HttpServletRequest req) throws InputOutputDataException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String lineJson;
            while ((lineJson = reader.readLine()) != null) {
                stringBuilder.append(lineJson);
            }
        } catch (IOException exception) {
            throw new InputOutputDataException(String.format("Erro ao processar payload da requisição: %s", exception.getMessage()), HttpStatus.BAD_REQUEST_400);
        }

        return stringBuilder;
    }

    /**
     * <h3>Método reescrito para uma falsa requisição PUT, a requisição no caso atende parecido a um patch, não é usado o patch devido a erros 501 do jetty</h3>
     * <p>É extraído o ID do livro a ser alterado e após isso válida o ID</p>
     * <p>Após pegar o id do livro é feito a leitura do payload da requisição</p>
     *
     * @param req  -> <strong>Requisição do usuário</strong>
     * @param resp -> <strong>Resposta a ser retornada</strong>
     * @throws ServletException -> <strong>Tratamento de erros http</strong>
     * @throws IOException      -> <strong>Tratamento de erros de entrada e saida de dados</strong>
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String extractIdBook;
        Long idBook;

        // Captura exceções e envia resposta em JSON
        try {
            // Extrai o ID do livro da URL e valida
            extractIdBook = extractIdBook(req);
            idBook = ValidateUrlParameter.validateLongId(extractIdBook);

            // Lê o payload da requisição
            StringBuilder stringBuilder = getStringBuilder(req);

            // Converte o JSON para um objeto BookRequest
            BookRequest bookRequest = ManipulateJsonObject.readJson(stringBuilder, BookRequest.class);

            String jsonBook;

            // Atualiza o livro
            bookCrudService.putBook(bookRequest, idBook);

            // Recupera o livro atualizado
            char[] bookChars = bookCrudService.getBookById(idBook);
            if (bookChars == null) {
                throw new MalformedJsonException("Erro ao montar json com o livro!", HttpStatus.INTERNAL_SERVER_ERROR_500);
            }
            jsonBook = new String(bookChars);  // Converte char[] para String

            // Envia a resposta com o livro atualizado
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonBook);

        } catch (RuntimeException exception) {
            JacksonErrorHandler.handleException(exception, resp);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
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
