package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.enums.ConversationRole;
import com.e_messenger.code.entity.enums.DetailConvNotiType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TypeAlias("changeRole")
public class ChangeRole extends ConversationNotification {
    ConversationRole fromRole;
    ConversationRole toRole;
    List<Participant> affectedParticipants;

    public ChangeRole(String actorId, String actorName, String conversationId, LocalDateTime time, ConversationRole fromRole, ConversationRole toRole, List<Participant> affectedParticipants) {
        super("Participants's role has been changed", actorId, actorName, conversationId, time, DetailConvNotiType.CHANGE_ROLE);
        this.fromRole = fromRole;
        this.toRole = toRole;
        this.affectedParticipants = affectedParticipants;
    }
}
