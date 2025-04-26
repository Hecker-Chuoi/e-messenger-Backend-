package com.e_messenger.code.entity.enums;

import lombok.Getter;

@Getter
public enum ConversationRole {
    OWNER(5),
    CO_OWNER(2),
    MEMBER(1);

    final int power;

    ConversationRole(int power) {
        this.power = power;
    }
}
