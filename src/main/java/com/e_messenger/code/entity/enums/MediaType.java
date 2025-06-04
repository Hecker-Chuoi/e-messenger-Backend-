package com.e_messenger.code.entity.enums;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum MediaType {
    IMAGE ("image"),
    AUDIO ("video");

    final String uploadOption;

    MediaType(String uploadOption) {
        this.uploadOption = uploadOption;
    }

    @JsonCreator
    public static MediaType fromString(String mediaType){
        if(mediaType.isBlank())
            throw new AppException(StatusCode.UNCATEGORIZED);
        try{
            return MediaType.valueOf(mediaType.toUpperCase());
        }
        catch(Exception e){
            throw new AppException(StatusCode.UNCATEGORIZED);
        }
    }
}
