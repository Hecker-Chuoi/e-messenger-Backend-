package com.e_messenger.code.utils.validation;

import com.e_messenger.code.utils.validation.validator.PhoneValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PhoneValidation {
    String message() default "Email invalid";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
