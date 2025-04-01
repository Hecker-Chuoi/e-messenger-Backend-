package com.e_messenger.code.exception;

import lombok.Getter;

@Getter
public enum StatusCode {
    OK(0, "OK"),

    USERNAME_USED(100, "Please choose another username"),
    PHONE_USED(101, "Please choose another phone number"),
    USER_NOT_FOUND(102, "User not found"),
    UNAUTHENTICATED(103, "Unauthenticated"),

    UNCATEGORIZED(999, "Uncategorized error");

    final int code;
    final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
