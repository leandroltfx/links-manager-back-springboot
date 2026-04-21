package br.com.links_manager_back_springboot.annotation;

import br.com.links_manager_back_springboot.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default "Nome de usuário inválido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
