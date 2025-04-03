package com.e_messenger.code.utils.jackson.deserializer;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
        try {
            String text = jsonParser.getText();
            return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        catch(DateTimeParseException e){
            throw new AppException(StatusCode.INVALID_DATE_FORMAT);
        }
    }
}
