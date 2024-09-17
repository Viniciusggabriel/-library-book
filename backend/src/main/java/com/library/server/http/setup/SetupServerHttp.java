package com.library.server.http.setup;

import com.library.server.http.handlers.BookHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class SetupServerHttp {

    public void startServer(String host, int port) throws Exception {
        Server server = new Server();

        // Define o connector http da aplicação
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(host);
        connector.setPort(port);
        server.addConnector(connector);

        // Define o handler para os livros
        BookHandler bookHandler = new BookHandler();
        server.setHandler(bookHandler.setupBookHandler());

        server.start();
    }
}
