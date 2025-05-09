package com.e_messenger.code.entity.enums;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    TEXT ("text"),
    IMAGE ("image"),
    AUDIO ("video");

    final String uploadOption;

    MessageType(String uploadOption) {
        this.uploadOption = uploadOption;
    }

    public String getUploadOption() {
        return uploadOption;
    }

    @JsonCreator
    public static MessageType fromString(String text) {
        if(text.isBlank())
            throw new AppException(StatusCode.UNCATEGORIZED);
        try{
            return MessageType.valueOf(text.toUpperCase());
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
