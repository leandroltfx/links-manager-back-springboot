package br.com.links_manager_back_springboot.annotation;

import br.com.links_manager_back_springboot.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Informe a senha.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
