package com.library.application.controllers;

import com.library.application.services.BookCrudService;
import com.library.util.errors.exceptions.InvalidRequestPathParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class GetBookByIdController extends HttpServlet {
    private final BookCrudService bookCrudService;

    public GetBookByIdController() {
        this.bookCrudService = new BookCrudService();
    }

    /**
     * <h3>Método que define um verbo get do protocolo HTTP e define a resposta como json e realiza no momento respostas vagas para testes</h3>
     *
     * @param req  -> <strong>Requisição do usuário</strong>
     * @param resp -> <strong>Resposta a ser retornada</strong>
     * @throws ServletException -> <strong>Tratamento de erros http</strong>
     * @throws IOException      -> <strong>Tratamento de erros de entrada e saida de dados</strong>
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String extractIdBook;
        Long idBook;

        try {
            extractIdBook = extractIdBook(req);
            idBook = validateIdBook(extractIdBook);
        } catch (InvalidRequestPathParameterException exception) {
            throw new InvalidRequestPathParameterException(exception.getMessage(), exception.getHttpStatus(), exception.getCause());
        }

        char[] jsonBook;
        try {
            jsonBook = bookCrudService.getBookById(idBook);

            if (jsonBook == null)
                throw new ServletException("Erro ao montar json com o livro!");
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
        return Optional.ofNullable(req.getPathInfo())
                .map(path -> path.substring(1)) // Remove a barra inicial
                .orElseThrow(() -> new InvalidRequestPathParameterException("Não foi possível extrair o ID do livro na sua requisição!", HttpStatus.BAD_REQUEST_400));
    }

    /**
     * <h3>Método para validar se o valor pego pela requisição está correto</h3>
     * <p>Verifica se o valor achado é nulo</p>
     * <p>Tenta realizar a troca do valor para um Long, o valor do ID do livro também é um Long</p>
     *
     * @param idBook -> <strong>Valor encontrado na URL</strong>
     * @return Long -> <strong>Valor achado transformado em um Long</strong>
     * @throws InvalidRequestPathParameterException -> <strong>Exception personalizada que é lançada caso o valor inserido não corresponda ao esperado</strong>
     */
    private Long validateIdBook(String idBook) throws InvalidRequestPathParameterException {
        if (idBook == null) {
            throw new InvalidRequestPathParameterException("O id da requisição não pode ser nulo!", HttpStatus.BAD_REQUEST_400);
        }

        try {
            return Long.valueOf(idBook);
        } catch (NumberFormatException exception) {
            throw new InvalidRequestPathParameterException("O id da requisição deve ser um número válido!", HttpStatus.BAD_REQUEST_400, new RuntimeException(exception.getMessage()));
        }
    }
}
