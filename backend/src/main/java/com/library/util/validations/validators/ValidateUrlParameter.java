package com.library.util.validations.validators;

import com.library.util.errors.exceptions.InvalidRequestPathParameterException;
import org.eclipse.jetty.http.HttpStatus;

public class ValidateUrlParameter {
    /**
     * <h3>Método para validar se o valor pego pela requisição está correto</h3>
     * <p>Verifica se o valor achado é nulo</p>
     * <p>Tenta realizar a troca do valor para um Long, o valor do ID do livro também é um Long</p>
     *
     * @param idBook -> <strong>Valor encontrado na URL</strong>
     * @return Long -> <strong>Valor achado transformado em um Long</strong>
     * @throws InvalidRequestPathParameterException -> <strong>Exception personalizada que é lançada caso o valor inserido não corresponda ao esperado</strong>
     */
    public static Long validateLongId(String idBook) throws InvalidRequestPathParameterException {
        if (idBook == null) {
            throw new InvalidRequestPathParameterException("O id da requisição não pode ser nulo!", HttpStatus.BAD_REQUEST_400);
        }

        try {
            return Long.valueOf(idBook);
        } catch (NumberFormatException exception) {
            throw new InvalidRequestPathParameterException("O id da requisição deve ser um número válido!", HttpStatus.BAD_REQUEST_400, new RuntimeException(exception.getMessage()));
        }
    }
}
