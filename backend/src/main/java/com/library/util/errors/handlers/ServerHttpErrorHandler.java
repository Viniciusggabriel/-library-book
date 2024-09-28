package com.library.util.errors.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.library.util.errors.exceptions.InputOutputDataErros;
import com.library.util.errors.exceptions.ValueNotFound;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.ee10.servlet.ErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextRequest;
import org.eclipse.jetty.http.HttpException;
import org.eclipse.jetty.http.HttpStatus;
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
        response.setContentType("application/json;charset=utf-8");

        Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");

        ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
        ObjectNode jsonResponse = objectMapper.createObjectNode(); // Cria um nó JSON

        try (PrintWriter writer = response.getWriter()) {
            String errorMessage = (throwable != null && throwable.getMessage() != null) ? throwable.getMessage() : message;

            // Limpa a mensagem de erro, se necessário
            if (errorMessage.contains(":")) {
                errorMessage = errorMessage.substring(errorMessage.indexOf(":") + 1).trim();
            }

            if (throwable != null) {
                // Tentativa de obter o código de status da exceção
                if (throwable instanceof ValueNotFound) {
                    code = ((ValueNotFound) throwable).getHttpStatus();
                    response.setStatus(code);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }

            // Preenche o nó JSON com os dados
            jsonResponse.put("status", code);
            jsonResponse.put("message", errorMessage);

            // Escreve a resposta JSON
            writer.write(objectMapper.writeValueAsString(jsonResponse));
        } catch (IOException exception) {
            logger.error("Erro ao enviar resposta JSON: ", exception);
            throw new InputOutputDataErros("Erro ao realizar transição de dados: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
        } catch (Exception exception) {
            logger.error("Erro ao manipular requisição: ", exception);
            throw new InputOutputDataErros("Erro ao realizar transição de dados: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }
}
