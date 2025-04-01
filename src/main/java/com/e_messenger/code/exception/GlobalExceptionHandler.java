package com.e_messenger.code.exception;

import com.e_messenger.code.dto.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> fallbackExceptionHandling(Exception exception) {
        exception.printStackTrace();

        Throwable cause = getRootCause(exception);
        if(cause instanceof AppException appException) {
            return appExceptionHandling(appException);
        }

        return appExceptionHandling(new AppException(StatusCode.UNCATEGORIZED));
    }

    private Throwable getRootCause(Throwable throwable) {
        if(throwable.getCause() == null)
            return throwable;
        return getRootCause(throwable.getCause());
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> appExceptionHandling(AppException exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(
                ApiResponse.<AppException>builder()
                        .code(exception.getStatusCode().getCode())
                        .message(exception.getStatusCode().getMessage())
                        .build()
        );
    }
}
