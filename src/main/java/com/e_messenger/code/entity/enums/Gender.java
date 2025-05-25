package com.e_messenger.code.entity.enums;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE, FEMALE, OTHER;

    @JsonCreator
    public static Gender getGender(String gender) {
        if(gender.isBlank())
            throw new AppException(StatusCode.UNCATEGORIZED);
        try{
            return Gender.valueOf(gender.toUpperCase());
        }
        catch(IllegalArgumentException e){
            throw new AppException(StatusCode.UNCATEGORIZED);
        }
    }
}
