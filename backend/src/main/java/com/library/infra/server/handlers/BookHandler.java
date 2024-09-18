package com.library.infra.server.handlers;

import com.library.application.controllers.LibraryServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;

public class BookHandler {

    /**
     * Handler para definir as rotas dentro da api pra executar operações com os livros
     *
     * @return ServletContextHandler -> Retorna um context para ser usado pelo servidor
     */
    public static ServletContextHandler setupBookHandler() {
        // Define Jarkarta context handler
        ServletContextHandler servletContextHandler = new ServletContextHandler();

        // Define as rotas da api
        servletContextHandler.setContextPath("/v1/");
        servletContextHandler.addServlet(LibraryServlet.class, "/list/book");

        return servletContextHandler;
    }
}
