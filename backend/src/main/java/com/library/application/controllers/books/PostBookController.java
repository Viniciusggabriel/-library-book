package com.library.application.controllers.books;

import com.library.application.dto.requests.BookRequest;
import com.library.application.services.BookCrudService;
import com.library.util.errors.exceptions.InputOutputDataException;
import com.library.util.utilitarian.ManipulateJsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class PostBookController extends HttpServlet {
    private final BookCrudService bookCrudService;

    public PostBookController() {
        this.bookCrudService = new BookCrudService();
    }

    /**
     * <h3>Método para inserir livros dentro do banco de dados</h3>
     * <p>Le o payload da requisição, e chamar o service com os valores obtidos</p>
     *
     * @param req  -> <strong>Requisição do usuário</strong>
     * @param resp -> <strong>Resposta a ser retornada</strong>
     * @throws ServletException -> <strong>Tratamento de erros http</strong>
     * @throws IOException      -> <strong>Tratamento de erros de entrada e saida de dados</strong>
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String lineJson;
            while ((lineJson = reader.readLine()) != null) {
                stringBuilder.append(lineJson);
            }
        } catch (IOException exception) {
            throw new InputOutputDataException(String.format("Erro ao processar payload da requisição: %s", exception.getMessage()), HttpStatus.BAD_REQUEST_400);
        }

        BookRequest bookRequest = ManipulateJsonObject.generateJson(stringBuilder, BookRequest.class);
        bookCrudService.postBook(bookRequest);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Sucesso ao criar o livro!");
    }
}
