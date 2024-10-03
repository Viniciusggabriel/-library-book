package com.library.util.utilitarian;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.util.errors.exceptions.InvalidJsonPropertyException;
import org.eclipse.jetty.http.HttpStatus;

public class CreateJsonObject {

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
}
