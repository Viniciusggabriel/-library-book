package com.library.application.controllers.books;

import com.library.application.services.BookCrudService;
import com.library.util.utilitarian.ManipulateJsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class GetAllBooksController extends HttpServlet {
    private final BookCrudService bookCrudService;

    public GetAllBooksController() {
        this.bookCrudService = new BookCrudService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sizeParam = req.getParameter("size");
        String pageParam = req.getParameter("page");

        int size = (sizeParam != null) ? Integer.parseInt(sizeParam) : 10;
        int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;

        List<char[]> jsonBooks = bookCrudService.getAllBooks(size, page);

        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(ManipulateJsonObject.generateListJson(jsonBooks));
    }
}
