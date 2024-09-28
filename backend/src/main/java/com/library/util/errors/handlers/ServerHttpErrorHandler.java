package com.library.util.errors.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.ee10.servlet.ErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

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
        // Definir tipo de conteúdo e código de status
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(code);

        // Pegar a exceção da request, se existir no caso o Throwable
        Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");

        // Extrair apenas a mensagem da exceção, sem o nome da classe
        String errorMessage = (throwable != null) ? throwable.getMessage() : message;

        // Escrever a resposta em JSON
        try (PrintWriter writer = response.getWriter()) {
            String jsonResponse = String.format("{\"status\": %d, \"message\": \"%s\"}", code, errorMessage);
            writer.write(jsonResponse);

        } catch (Exception exception) {
            logger.error("Não foi possível enviar erros ao usuário!: ", exception);
        }
    }
}
