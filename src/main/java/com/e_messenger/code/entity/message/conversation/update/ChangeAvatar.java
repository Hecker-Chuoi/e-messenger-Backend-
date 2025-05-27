package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TypeAlias("changeAvatar")
public class ChangeAvatar extends ConversationNotification {
    @Override
    public DetailActionType getActionType() {
        return DetailActionType.CHANGE_AVATAR;
    }

}
