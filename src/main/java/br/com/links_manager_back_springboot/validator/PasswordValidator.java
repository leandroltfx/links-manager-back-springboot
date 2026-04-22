package br.com.links_manager_back_springboot.validator;

import br.com.links_manager_back_springboot.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (value == null || value.isEmpty()) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Informe a senha.")
                    .addConstraintViolation();
            return false;
        }

        if (value.length() < 8 || value.length() > 60) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("A senha deve ter entre 8 e 60 caracteres.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
