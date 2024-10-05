package com.library.util.errors.throwables;

import lombok.Getter;

@Getter
public class ApplicationRuntimeException extends RuntimeException {
    private final int statusCode;

    /**
     * <h3>Exception que serve como base, é usada para poder ser capturada no handler de erros</h3>
     * <p>Para usar essa exception é só extender ela em outra exception que quando a exceção for lançada ela será enviada para o usuário via HTTP</p>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public ApplicationRuntimeException(String message, Integer httpStatus) {
        super(message);
        this.statusCode = httpStatus;
    }
}
