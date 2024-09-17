package com.library.server.http.handlers;

import com.library.application.controllers.LibraryServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;

public class BookHandler {

    public ServletContextHandler setupBookHandler() {
        // Define Jarkarta context handler
        ServletContextHandler servletContextHandler = new ServletContextHandler();

        // Define as rotas da api
        servletContextHandler.setContextPath("/v1/");
        servletContextHandler.addServlet(LibraryServlet.class, "/list/book");

        return servletContextHandler;
    }
}
