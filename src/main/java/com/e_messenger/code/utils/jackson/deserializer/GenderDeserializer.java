package com.e_messenger.code.utils.jackson.deserializer;

import com.e_messenger.code.entity.enums.Gender;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class GenderDeserializer extends JsonDeserializer<Gender> {
    @Override
    public Gender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException{
        String value = p.getValueAsString().toUpperCase();
        if(value.isBlank()) return Gender.OTHER;
        try{
            return Gender.valueOf(value);
        }
        catch(IllegalArgumentException e){
            throw new AppException(StatusCode.UNCATEGORIZED);
        }
    }
}
