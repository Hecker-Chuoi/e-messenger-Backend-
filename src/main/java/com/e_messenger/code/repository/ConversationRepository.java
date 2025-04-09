package com.e_messenger.code.repository;

import com.e_messenger.code.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findConversationById(String id);
    @Query("{'id': {$regex: ?0}}")
    List<Conversation> getAllDirectChat(String pattern);

    @Query("{'participantIds': ?0, type : ?1}")
    List<Conversation> getConversationByParticipantIdsContainingAndType(String id, Conversation.ConversationType type);

    @Query("{'participantIds': ?0}")
    List<Conversation> getConversationByParticipantIdsContaining(String id);
}
