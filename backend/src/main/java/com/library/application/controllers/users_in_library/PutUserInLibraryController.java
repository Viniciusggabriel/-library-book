package com.library.application.controllers.users_in_library;

import com.library.application.dto.requests.UserInLibraryRequest;
import com.library.application.services.UserInLibraryCrudService;
import com.library.util.errors.exceptions.InputOutputDataException;
import com.library.util.errors.exceptions.InvalidRequestPathParameterException;
import com.library.util.errors.handlers.JacksonErrorHandler;
import com.library.util.utilitarian.ManipulateJsonObject;
import com.library.util.validations.validators.ValidateUrlParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

// TODO: Verificar o por que não estar realizando método corrétamente
@RequiredArgsConstructor
public class PutUserInLibraryController extends HttpServlet {
    private final UserInLibraryCrudService userInLibraryCrudService;

    public PutUserInLibraryController() {
        this.userInLibraryCrudService = new UserInLibraryCrudService();
    }

    private static StringBuilder getStringBuilder(HttpServletRequest req) throws InputOutputDataException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String lineJson;
            while ((lineJson = reader.readLine()) != null) {
                stringBuilder.append(lineJson);
            }
        } catch (IOException exception) {
            throw new InputOutputDataException(String.format("Erro ao processar payload da requisição: %s", exception.getMessage()), HttpStatus.BAD_REQUEST_400);
        }

        return stringBuilder;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String extractIdUser;
        UUID idUser;

        try {
            extractIdUser = extractIdUser(req);
            idUser = ValidateUrlParameter.validateUUIDId(extractIdUser);

            StringBuilder stringBuilder = getStringBuilder(req);

            UserInLibraryRequest userInLibraryRequest = ManipulateJsonObject.readJson(stringBuilder, UserInLibraryRequest.class);

            userInLibraryCrudService.putUserInLibrary(userInLibraryRequest, idUser);

            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Sucesso ao alterar o usuário!");

        } catch (RuntimeException exception) {
            JacksonErrorHandler.handleException(exception, resp);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String extractIdUser(HttpServletRequest req) throws InvalidRequestPathParameterException {
        return Optional.ofNullable(req.getPathInfo()).map(path -> path.substring(1)) // Remove a barra inicial
                .orElseThrow(() -> new InvalidRequestPathParameterException("Não foi possível extrair o ID do usuário na sua requisição!", HttpStatus.BAD_REQUEST_400));
    }
}
