package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.enums.GeneralType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class ConversationNotification extends Message {
    DetailActionType actionType;
}
