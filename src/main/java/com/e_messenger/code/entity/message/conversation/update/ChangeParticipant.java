package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TypeAlias("changeParticipant")
public class ChangeParticipant extends ConversationNotification {

    public enum Method{
        ADD, REMOVE
    }

    Method method;
    List<String> affectedParticipants;
}
