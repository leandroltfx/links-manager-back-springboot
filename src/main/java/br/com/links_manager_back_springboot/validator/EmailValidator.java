package br.com.links_manager_back_springboot.validator;

import br.com.links_manager_back_springboot.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = "^(?i)(?=.{1,254}$)(?=.{1,64}@)[a-z0-9.]+@[a-z0-9]+\\.[a-z]+(\\.[a-z]+)?$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (value == null || value.isEmpty()) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Informe o e-mail.")
                    .addConstraintViolation();
            return false;
        }

        if (value.length() > 254) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("O e-mail não deve ultrapassar 254 caracteres.")
                    .addConstraintViolation();
            return false;
        }

        if (!value.matches(EMAIL_PATTERN)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("E-mail inválido.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

