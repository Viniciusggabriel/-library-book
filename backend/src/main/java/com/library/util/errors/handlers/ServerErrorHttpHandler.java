package com.library.util.errors.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.ee10.servlet.ErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class ServerErrorHttpHandler extends ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerErrorHttpHandler.class);

    /**
     * Método responsável por retornar erros que serão lançados para o usuário caso os controllers não consigam validar a requisição
     *
     * @param baseRequest -> Serve para pegar os dados base da requisição
     * @param request     -> Pega os valores que foram requisitado
     * @param response    -> Define o que será respondido
     * @param code        -> Define o status code a ser usado
     * @param message     -> Define a mensagem quer será retornada
     * @throws IOException -> Exception de entrada é saida de dados
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
            logger.error("Erro ao mandar erros de requisição para o usuário!: ", exception);
        }
    }
}
