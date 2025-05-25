package com.e_messenger.code.entity.enums;

public enum MediaType {
    IMAGE ("image"),
    AUDIO ("video");

    final String uploadOption;

    MediaType(String uploadOption) {
        this.uploadOption = uploadOption;
    }
}
