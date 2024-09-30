package com.library.util.errors.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.library.util.errors.throwables.ApplicationRuntimeException;
import com.library.util.errors.throwables.ApplicationServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.ee10.servlet.ErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;

public class ServerHttpErrorHandler extends ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerHttpErrorHandler.class);

    /**
     * <h3>Método responsável por retornar erros que serão lançados para o usuário caso os controllers não consigam validar a requisição.</h3>
     *
     * @param baseRequest -> <strong>Serve para pegar os dados base da requisição</strong>
     * @param request     -> <strong>Pega os valores que foram requisitado</strong>
     * @param response    -> <strong>Define o que será respondido</strong>
     * @param code        -> <strong>Define o status code a ser usado</strong>
     * @param message     -> <strong>Define a mensagem quer será retornada</strong>
     * @throws IOException -> <strong>Exception de entrada é saida de dados</strong>
     */
    @Override
    protected void generateAcceptableResponse(ServletContextRequest baseRequest, HttpServletRequest request, HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        int statusCode = code != 0 ? code : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        Throwable cause = baseRequest.read().getFailure();

        Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        Throwable rootCause = throwable != null ? getRootCause(throwable) : null;

        if (throwable instanceof ApplicationRuntimeException applicationRuntimeException) {
            statusCode = applicationRuntimeException.getStatusCode();
            message = applicationRuntimeException.getMessage();

        } else if (throwable instanceof ApplicationServletException applicationServletException) {
            statusCode = applicationServletException.getHttpStatus();
            message = applicationServletException.getMessage();
            cause = applicationServletException.getCause();

        } else if (rootCause != null) {
            message = rootCause.getMessage();
            if (rootCause instanceof ApplicationRuntimeException appException) {
                statusCode = appException.getStatusCode();
            }
        }

        response.setStatus(statusCode);
        jsonResponse.put("status", statusCode);
        jsonResponse.put("message", message != null ? message : "Erro desconhecido");

        if (cause != null) {
            jsonResponse.put("cause", cause.getMessage());
        }

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(jsonResponse));
        } catch (IOException exception) {
            logger.error("Erro ao enviar resposta JSON: ", exception);
            throw new IOException("Erro ao enviar resposta: " + exception.getMessage());
        }
    }
}
