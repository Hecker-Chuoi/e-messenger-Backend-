package com.e_messenger.code.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppException extends RuntimeException {
    private StatusCode statusCode;

    public AppException(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
