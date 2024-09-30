package com.library.util.errors.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityAttributeAccessException extends RuntimeException {
    private final Integer httpStatus;

    /**
     * <h3>Exception que serve como base, é usada para poder ser capturada no handler de erros</h3>
     * <p>Para usar essa exception é só extender ela em outra exception que quando a exceção for lançada ela será enviada para o usuário via HTTP</p>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public EntityAttributeAccessException(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
        this.httpStatus = httpStatus;
    }
}
