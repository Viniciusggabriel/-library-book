package com.library.infra.server.handlers;

import com.library.application.controllers.users_in_library.AuthenticateUserInLibraryController;
import com.library.application.controllers.users_in_library.PostUserInLibraryController;
import com.library.application.controllers.users_in_library.PutUserInLibraryController;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;

public class UserInLibraryHandlers {
    public static ServletContextHandler setupUserInLibraryHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler();

        servletContextHandler.setContextPath("/v1/");
        servletContextHandler.addServlet(new ServletHolder(new PostUserInLibraryController()), "/insert/user_in_library");
        servletContextHandler.addServlet(new ServletHolder(new PutUserInLibraryController()), "/update/user_in_library/*");
        servletContextHandler.addServlet(new ServletHolder(new AuthenticateUserInLibraryController()), "/authenticate/user_in_library");

        return servletContextHandler;
    }
}
