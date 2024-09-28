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
        String idBook = extractIdBook(req);
        validateIdBook(idBook);

        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(bookCrudService.getBookById(idBook));
    }

    private String extractIdBook(HttpServletRequest req) throws RequestPathIllegalReference {
        return Optional.ofNullable(req.getPathInfo())
                .map(path -> path.substring(1))
                .orElseThrow(() -> new RequestPathIllegalReference("Não foi possível extrair o ID do livro na sua requisição!", HttpStatus.BAD_REQUEST_400));
    }

    private void validateIdBook(String idBook) throws RequestPathIllegalReference {
        if (idBook == null || idBook.isEmpty()) {
            throw new RequestPathIllegalReference("O id da requisição não pode ser nulo!");
        }
    }
}
