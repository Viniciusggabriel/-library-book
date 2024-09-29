package com.library.application.controllers;

import com.library.application.services.BookCrudService;
import com.library.util.errors.exceptions.RequestPathIllegalReference;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class ServletGetBookById extends HttpServlet {
    private final BookCrudService bookCrudService;


    public ServletGetBookById() {
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
        } catch (RequestPathIllegalReference exception) {
            throw new RequestPathIllegalReference(exception.getMessage(), exception.getStatusCode(), exception.getCause());
        }

        char[] jsonBook;
        try {
            jsonBook = bookCrudService.getBookById(idBook);

            if (jsonBook == null)
                throw new ServletException("Erro ao montar json com o livro!");
        } catch (Exception exception) {
            throw new RequestPathIllegalReference(exception.getMessage(), HttpStatus.BAD_GATEWAY_502, exception.getCause());
        }

        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(jsonBook);
    }

    private String extractIdBook(HttpServletRequest req) throws RequestPathIllegalReference {
        return Optional.ofNullable(req.getPathInfo())
                .map(path -> path.substring(1)) // Remove a barra inicial
                .orElseThrow(() -> new RequestPathIllegalReference("Não foi possível extrair o ID do livro na sua requisição!", HttpStatus.BAD_REQUEST_400));
    }

    private Long validateIdBook(String idBook) throws RequestPathIllegalReference {
        if (idBook == null) {
            throw new RequestPathIllegalReference("O id da requisição não pode ser nulo!", HttpStatus.BAD_REQUEST_400);
        }

        try {
            return Long.valueOf(idBook);
        } catch (NumberFormatException exception) {
            throw new RequestPathIllegalReference("O id da requisição deve ser um número válido!", HttpStatus.BAD_REQUEST_400, new RuntimeException(exception.getMessage()));
        }
    }
}
