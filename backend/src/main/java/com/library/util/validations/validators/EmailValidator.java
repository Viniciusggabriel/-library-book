package com.library.util.validations.validators;

import com.library.util.validations.anotations.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    /**
     * <h3>Método para validar pattern de email</h3>
     *
     * @param email   -> <strong>Numero de email a ser validado</strong>
     * @param context -> <strong>Contexto a ser retornado</strong>
     * @return boolean -> <strong>Status da validação</strong>
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }
}
