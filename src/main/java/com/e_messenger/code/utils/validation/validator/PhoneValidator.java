package com.e_messenger.code.utils.validation.validator;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.utils.validation.PhoneValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneValidation, String> {
    private final String pattern = "^0[0-9]{9}$";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(!s.matches(pattern))
            throw new AppException(StatusCode.PHONE_FORMAT_INVALID);
        return true;
    }
}
