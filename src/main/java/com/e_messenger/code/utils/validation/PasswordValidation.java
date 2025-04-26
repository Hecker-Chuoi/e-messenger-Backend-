package com.e_messenger.code.utils.validation;

import com.e_messenger.code.utils.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidation {
    String message() default "Password invalid";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
