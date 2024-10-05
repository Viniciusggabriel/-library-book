package com.library.util.errors.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.library.util.errors.throwables.ApplicationRuntimeException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JacksonErrorHandler {
    public static void handleException(RuntimeException exception, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(exception instanceof ApplicationRuntimeException ? ((ApplicationRuntimeException) exception).getStatusCode() : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        jsonResponse.put("message", exception.getMessage());
        jsonResponse.put("status", response.getStatus());

        if (exception.getCause() != null) {
            jsonResponse.put("cause", exception.getCause().getMessage());
        }

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(jsonResponse));
        }
    }
}
