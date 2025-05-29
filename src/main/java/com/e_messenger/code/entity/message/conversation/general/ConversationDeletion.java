package com.e_messenger.code.entity.message.conversation.general;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@TypeAlias("conversationDeletion")
public class ConversationDeletion extends ConversationNotification {
    String name;
}
