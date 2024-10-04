package com.library.util.utilitarian;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.util.errors.exceptions.InvalidJsonPropertyException;
import com.library.util.errors.exceptions.MalformedJsonException;
import org.eclipse.jetty.http.HttpStatus;

public class ManipulateJsonObject {

    public static <T> T generateJson(StringBuilder stringBuilder, Class<T> tClass) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            return objectMapper.readValue(stringBuilder.toString(), tClass);
        } catch (UnrecognizedPropertyException exception) {
            throw new InvalidJsonPropertyException(String.format("Json malformado verifique as chaves e valores do seu Json: %s",
                    exception.getPropertyName()), HttpStatus.BAD_REQUEST_400, exception.getCause());
        }
    }

    public static <T> char[] readJson(T object) throws MalformedJsonException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json;

        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new MalformedJsonException(exception.getMessage(), exception.getLocation(), HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        return json.toCharArray();
    }
}
