package com.e_messenger.code.repository;

import com.e_messenger.code.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, ObjectId> {
    List<Message> getMessagesByGroupId(String groupId);
}
