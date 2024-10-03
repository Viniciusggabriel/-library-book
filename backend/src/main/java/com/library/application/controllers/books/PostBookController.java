package com.library.application.controllers.books;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.application.dto.requests.BookRequest;
import com.library.application.services.BookCrudService;
import com.library.util.errors.exceptions.InvalidJsonPropertyException;
import com.library.util.utilitarian.CreateJsonObject;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String lineJson;
        while ((lineJson = reader.readLine()) != null) {
            stringBuilder.append(lineJson);
        }

        BookRequest bookRequest = CreateJsonObject.generateJson(stringBuilder, BookRequest.class);
        bookCrudService.postBook(bookRequest);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Sucesso ao criar o livro!");
    }
}
