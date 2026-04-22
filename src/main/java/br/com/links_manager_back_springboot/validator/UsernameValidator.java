package br.com.links_manager_back_springboot.validator;

import br.com.links_manager_back_springboot.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final String USERNAME_PATTERN = "^(?!.*--)[A-Za-z0-9](?:[A-Za-z0-9-]{1,28}[A-Za-z0-9])$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (value == null || value.isEmpty()) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Informe o nome de usuário.")
                    .addConstraintViolation();
            return false;
        }

        if (value.length() < 3 || value.length() > 30) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("O nome de usuário deve ter entre 3 e 30 caracteres.")
                    .addConstraintViolation();
            return false;
        }

        if (!value.matches(USERNAME_PATTERN)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            "O nome de usuário deve ter apenas letras, números e hífen (-). Não pode começar ou terminar com hífen nem conter hífens consecutivos."
                    )
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
