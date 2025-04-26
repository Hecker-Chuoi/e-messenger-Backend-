package com.e_messenger.code.repository;

import com.e_messenger.code.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, ObjectId> {
    Page<Message> findMessagesByConversationId(String conversationId, Pageable pageable);
}
