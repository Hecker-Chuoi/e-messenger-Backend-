package com.e_messenger.code.utils.validation;

import com.e_messenger.code.utils.validation.validator.EmailValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValidation{
    String message() default "Email invalid";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
