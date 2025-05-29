package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.enums.GeneralType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class ConversationNotification extends Message {
    DetailActionType actionType;
}
