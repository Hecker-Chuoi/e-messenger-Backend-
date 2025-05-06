package com.e_messenger.code.exception;

import com.e_messenger.code.dto.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleFallbackException(Exception exception) {
        exception.printStackTrace();

        Throwable cause = getRootCause(exception);
        if(cause instanceof AppException appException) {
            return handleAppException(appException);
        }

        return handleAppException(new AppException(StatusCode.UNCATEGORIZED));
    }

    private Throwable getRootCause(Throwable throwable) {
        if(throwable.getCause() == null)
            return throwable;
        return getRootCause(throwable.getCause());
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(
                ApiResponse.<AppException>builder()
                        .code(exception.getStatusCode().getCode())
                        .message(exception.getStatusCode().getMessage())
                        .build()
        );
    }

    @MessageExceptionHandler(Exception.class)
    public void handleFallBackExceptionWs(Exception e) {
        e.printStackTrace();
    }
}
