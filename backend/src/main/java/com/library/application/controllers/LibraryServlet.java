package com.library.application.controllers;

import com.library.util.errors.exceptions.PortHttpServerException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public class LibraryServlet extends HttpServlet {
    /**
     * Método que define um verbo get do protocolo HTTP e define a resposta como json e realiza no momento respostas vagas para testes
     *
     * @param req  -> Requisição do usuário
     * @param resp -> Resposta a ser retornada
     * @throws ServletException -> Tratamento de erros http
     * @throws IOException      -> Tratamento de erros de entrada e saida de dados
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        if (Objects.equals(req.getMethod(), "get")) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new PortHttpServerException("Erro de teste");
        }

        resp.getWriter().write("Ola");
    }
}
