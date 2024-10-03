package com.library.infra.server.handlers;

import com.library.application.controllers.books.GetBookByIdController;
import com.library.application.controllers.books.PostBookController;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;

public class BookHandlers {

    /**
     * <h3>Handler para definir as rotas dentro da api pra executar operações com os livros</h3>
     *
     * @return ServletContextHandler -> <strong>Retorna um context para ser usado pelo servidor</strong>
     */
    public static ServletContextHandler setupBookHandler() {
        // Define Jarkarta context handler
        ServletContextHandler servletContextHandler = new ServletContextHandler();

        // Define a rota da api para buscar um livro pelo ID
        servletContextHandler.setContextPath("/v1/");
        servletContextHandler.addServlet(new ServletHolder(new GetBookByIdController()), "/list/book/*");
        servletContextHandler.addServlet(new PostBookController(), "/insert/book");

        return servletContextHandler;
    }
}
