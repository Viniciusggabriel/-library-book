package com.library.util.validations.validators;

import com.library.util.validations.anotations.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final String EMAIL_PATTERN = "^[0-9]{2}-([0-9]{8}|[0-9]{9})";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    /**
     * <h3>Método para validar pattern de numero de telefone +dd</h3>
     *
     * @param phoneNumber -> <strong>Numero de telefone a ser validado</strong>
     * @param context     -> <strong>Contexto a ser retornado</strong>
     * @return boolean -> <strong>Status da validação</strong>
     */
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false;
        }
        return phoneNumber.matches(EMAIL_PATTERN);
    }
}
