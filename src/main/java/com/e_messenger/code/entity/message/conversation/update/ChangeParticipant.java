package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.Participant;
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
@TypeAlias("changeParticipant")
public class ChangeParticipant extends ConversationNotification {
    public ChangeParticipant(String actorId, String actorName, String conversationId, LocalDateTime time, Method method, List<Participant> affectedParticipants) {
        super("Participants updated", actorId, actorName, conversationId, time, DetailConvNotiType.CHANGE_PARTICIPANTS);
        this.method = method;
        this.affectedParticipants = affectedParticipants;
    }

    enum Method{
        ADD, REMOVE
    }

    Method method;
    List<Participant> affectedParticipants;
}
