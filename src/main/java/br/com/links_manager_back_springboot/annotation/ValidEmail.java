package br.com.links_manager_back_springboot.annotation;

import br.com.links_manager_back_springboot.validator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "E-mail inválido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
