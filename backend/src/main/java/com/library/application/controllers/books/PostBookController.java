package com.library.application.controllers.books;

import com.library.application.dto.requests.BookRequest;
import com.library.application.services.BookCrudService;
import com.library.util.utilitarian.ManipulateJsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

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

        BookRequest bookRequest = ManipulateJsonObject.generateJson(stringBuilder, BookRequest.class);
        bookCrudService.postBook(bookRequest);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Sucesso ao criar o livro!");
    }
}
