package com.e_messenger.code.entity.enums;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GeneralType {
    TEXT,
    MEDIA,
    CONVERSATION;

    @JsonCreator
    public static GeneralType fromString(String text) {
        if(text.isBlank())
            throw new AppException(StatusCode.UNCATEGORIZED);
        try{
            return GeneralType.valueOf(text.toUpperCase());
        }
        catch(IllegalArgumentException e){
            throw new AppException(StatusCode.UNCATEGORIZED);
        }
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
