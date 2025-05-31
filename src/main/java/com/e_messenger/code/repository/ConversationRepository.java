package com.e_messenger.code.repository;

import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.enums.ConversationType;
import com.e_messenger.code.entity.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findConversationById(String id);
    @Query("{'id': {$regex: ?0}}")
    List<Conversation> findAllDirectChat(String pattern, Pageable pageable);

    @Query("{'participants.participantId': ?0, type : ?1}")
    List<Conversation> findConversationByParticipantIdsContainingAndType(String id, ConversationType type, Pageable pageable);

    @Query("{'participants.participantId': ?0}")
    List<Conversation> findConversationByParticipantIdsContaining(String id, Pageable pageable);
}
