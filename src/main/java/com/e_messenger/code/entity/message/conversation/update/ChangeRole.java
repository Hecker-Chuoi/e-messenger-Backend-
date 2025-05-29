package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.enums.ConversationRole;
import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@Data
@SuperBuilder
//@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TypeAlias("changeRole")
public class ChangeRole extends ConversationNotification {
    ConversationRole fromRole;
    ConversationRole toRole;
    List<String> affectedParticipants;

}
