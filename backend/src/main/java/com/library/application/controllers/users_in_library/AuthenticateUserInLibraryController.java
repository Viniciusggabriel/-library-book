package com.library.application.controllers.users_in_library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.application.dto.requests.UserInLibraryRequest;
import com.library.application.services.AuthenticateUserInLibraryService;
import com.library.util.errors.exceptions.InputOutputDataException;
import com.library.util.errors.exceptions.InvalidRequestPayloadException;
import com.library.util.utilitarian.ManipulateJsonObject;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.library.infra.security.JwtGenerateService.generateToken;
import static com.library.util.utilitarian.ManipulateJsonObject.generateJson;

public class AuthenticateUserInLibraryController extends HttpServlet {
    private final AuthenticateUserInLibraryService authenticateUserInLibraryService;

    public AuthenticateUserInLibraryController() {
        this.authenticateUserInLibraryService = new AuthenticateUserInLibraryService();
    }

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

        UserInLibraryRequest userInLibraryRequest = ManipulateJsonObject.readJson(stringBuilder, UserInLibraryRequest.class);
        boolean expectedAuthentication = authenticateUserInLibraryService.authenticateUserInLibrary(userInLibraryRequest);

        if (!expectedAuthentication) {
            throw new InvalidRequestPayloadException("A autenticação falhou!", HttpStatus.BAD_REQUEST_400);
        }

        try {
            String token = generateToken(userInLibraryRequest.dsUserName());
            Map<String, String> map = new HashMap<>();
            map.put("token", token);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(generateJson(map));
        } catch (JOSEException exception) {
            throw new RuntimeException(exception);
        }

    }

}
