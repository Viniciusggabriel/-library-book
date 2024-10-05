package com.library.util.utilitarian;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.util.errors.exceptions.InvalidJsonPropertyException;
import com.library.util.errors.exceptions.MalformedJsonException;
import org.eclipse.jetty.http.HttpStatus;

public class ManipulateJsonObject {

    /**
     * <h3>Método para gerar um json</h3>
     * <p>Recebe uma string com o texto obtido do payload e a classe de objeto que será retornada</p>
     *
     * @param stringBuilder -> <strong>String que será usada para montar o json</strong>
     * @param tClass        -> <strong>Classe de objeto que será usada para montar o json</strong>
     * @param <T>           -> <strong>Classe que será retornada do mesmo tipo da que é usada para montar o json</strong>
     * @return T -> <strong>Retorno baseado no tipo generico</strong>
     * @throws JsonProcessingException -> <strong>Exception para erros ao montar ojson</strong>
     */
    public static <T> T generateJson(StringBuilder stringBuilder, Class<T> tClass) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            return objectMapper.readValue(stringBuilder.toString(), tClass);
        } catch (UnrecognizedPropertyException exception) {
            throw new InvalidJsonPropertyException(
                    String.format("Json malformado verifique as chaves e valores do seu Json: %s", exception.getLocation()),
                    HttpStatus.BAD_REQUEST_400,
                    exception.getCause());

        } catch (JsonParseException exception) {
            throw new InvalidJsonPropertyException(
                    String.format("Erro ao realizar o parse do json: %s", exception.getMessage()),
                    HttpStatus.BAD_REQUEST_400,
                    exception.getCause());
        }
    }

    /**
     * <h3>Método para ler um json</h3>
     * <p>Recebe um objeto para verificar o que o usuário inseriu no payload da requisição</p>
     *
     * @param object -> <strong>Objeto a ser usado para ler</strong>
     * @param <T>    -> <strong>Valor genérico a ser recebi no objeto</strong>
     * @return char[] -> <strong>Json após ser lido é transformado em um array de char</strong>
     * @throws MalformedJsonException -> <strong>Exception para erros ao ler o json</strong>
     */
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
