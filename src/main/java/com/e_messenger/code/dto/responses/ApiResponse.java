package com.e_messenger.code.dto.responses;

import com.e_messenger.code.exception.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ApiResponse <T>{
    @Builder.Default
    int code = StatusCode.OK.getCode();
    @Builder.Default
    String message = StatusCode.OK.getMessage();
    T result;
}
