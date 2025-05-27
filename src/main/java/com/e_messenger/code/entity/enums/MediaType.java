package com.e_messenger.code.entity.enums;

public enum MediaType {
    IMAGE ("image"),
    AUDIO ("video");

    final String uploadOption;

    public String getUploadOption() {
        return uploadOption;
    }

    MediaType(String uploadOption) {
        this.uploadOption = uploadOption;
    }
}
