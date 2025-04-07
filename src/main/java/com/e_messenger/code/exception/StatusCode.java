package com.e_messenger.code.exception;

import lombok.Getter;

@Getter
public enum StatusCode {
    OK(0, "OK"),

    PHONE_USED(100, "This phone number has been used, please choose another phone number"),
    USER_NOT_FOUND(101, "User not found"),
    UNAUTHENTICATED(102, "Unauthenticated"),
    INVALID_PASSWORD(103, "Password must be 8-20 characters and has at least 1 lowercase, 1 uppercase, 1 number and 1 special characters"),
    PASSWORD_NOT_MATCH(104, "New password and confirmed password not match"),
    EMAIL_FORMAT_INVALID(105, "Please provide proper email"),
    PHONE_FORMAT_INVALID(106, "Please provide phone number in correct format, e.g. 0123456789"),

    INVALID_DATE_FORMAT(200, "Date format must be dd/MM/yyyy"),

    UNCATEGORIZED(999, "Uncategorized error");

    final int code;
    final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
