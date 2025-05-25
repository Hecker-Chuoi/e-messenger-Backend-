package com.e_messenger.code.utils;

import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.enums.ConversationRole;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParticipantUtil {
    UserService userService;

    public List<User> toUsers(List<Participant> participants) {
        return participants.stream()
                .map(e -> userService.getUserById(e.getParticipantId()))
                .collect(Collectors.toList());
    }

    public Participant toParticipants(User user, ConversationRole role){
        return Participant.builder()
                .participantId(user.getId())
                .displayName(user.getDisplayName())
                .phoneNumber(user.getPhoneNumber())
                .role(role)
                .joinAt(LocalDateTime.now())
                .build();
    }

    public List<Participant> toMembers(List<User> users){
        return users.stream().map(e -> toParticipants(e, ConversationRole.MEMBER))
                .collect(Collectors.toList());
    }

    public List<Participant> toDirectParticipants(User user1, User user2){
        return List.of(
                toParticipants(user1, ConversationRole.MEMBER),
                toParticipants(user2, ConversationRole.MEMBER)
        );
    }

    public List<Participant> toGroupParticipants(List<User> users){
        List<Participant> result = new ArrayList<>();
        result.add(toParticipants(users.getFirst(), ConversationRole.OWNER));
        for(int i = 1; i < users.size(); ++i){
            result.add(toParticipants(users.get(i), ConversationRole.MEMBER));
        }
        return result;
    }

    public boolean hasConversationAccess(Conversation conv, String userId){
        return conv.getParticipants().stream().anyMatch(e -> e.getParticipantId().equals(userId));
    }

    public ConversationRole getParticipantRole(Conversation conversation, String userId){
        List<Participant> participants = conversation.getParticipants();
        for(Participant x : participants){
            if(x.getParticipantId().equals(userId)){
                return x.getRole();
            }
        }
        return null;
    }

    public boolean hasRole(Conversation group, User user, ConversationRole role){
        return getParticipantRole(group, user.getId()).equals(role);
    }

    public boolean hasAnyRole(Conversation group, User user, List<ConversationRole> roles){
        return roles.contains(getParticipantRole(
                group, user.getId()));
    }

    public boolean hasManagementRole(Conversation group, User user){
        return List.of(
                ConversationRole.OWNER, ConversationRole.CO_OWNER).
                contains(getParticipantRole(group, user.getId()));
    }

    public boolean canAffect(Conversation group, User user, List<String> removeIds){
        if(!hasManagementRole(group, user)) return false;

        int max = 0;
        for(String id : removeIds){
            max = Integer.max(max, getParticipantRole(group, id).getPower());
        }
        return getParticipantRole(group, user.getId()).getPower() > max;
    }

    public void changeOwner(Conversation group, String oldOwnerId, String newOwnerId){
        for(Participant x : group.getParticipants()){
            if(x.getParticipantId().equals(oldOwnerId) && x.getRole().equals(ConversationRole.OWNER)){
                x.setRole(ConversationRole.MEMBER);
            }
            if(x.getParticipantId().equals(newOwnerId)){
                x.setRole(ConversationRole.OWNER);
            }
        }
    }

    public void setCoOwners(Conversation group, List<User> users){
        HashSet<String> set = new HashSet<>(users.stream().map(User::getId).collect(Collectors.toList()));

        for(Participant x : group.getParticipants()){
            if(set.contains(x.getParticipantId())){
                if(x.getRole().equals(ConversationRole.OWNER)){
                    throw new AppException(StatusCode.UNCATEGORIZED);
                }
                x.setRole(ConversationRole.CO_OWNER);
            }
        }
    }

    public void setMembers(Conversation group, List<User> users){
        HashSet<String> set = new HashSet<>(users.stream().map(User::getId).collect(Collectors.toList()));

        for(Participant x : group.getParticipants()){
            if(set.contains(x.getParticipantId())){
                if(x.getRole().equals(ConversationRole.OWNER)){
                    throw new AppException(StatusCode.UNCATEGORIZED);
                }
                x.setRole(ConversationRole.MEMBER);
            }
        }
    }
}
