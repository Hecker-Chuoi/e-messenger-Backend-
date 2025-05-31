package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ContactService {
    MongoTemplate mongoTemplate;

    public List<User> getDirectContacts(String userId, int pageSize, int pageNum) {
        Aggregation aggregation = Aggregation.newAggregation(
            // 1. Lọc các conversation DIRECT có userId tham gia
            Aggregation.match(
                Criteria.where("type").is("DIRECT")
                        .and("participants.participantId").is(userId)
            ),

            Aggregation.sort(Sort.by(Sort.Direction.DESC, "lastMessageTime")),

            // 2. Tách từng participant thành dòng riêng
            Aggregation.unwind("participants"),

            // 3. Lọc ra người còn lại (participant khác userId)
            Aggregation.match(
                Criteria.where("participants.participantId").ne(userId)
            ),

            // 4. Join sang collection "users" để lấy thông tin user còn lại
            Aggregation.lookup(
                "users",
                "participants.participantId",
                "_id",
                "userInfo"
            ),

            // 5. Bỏ mảng userInfo, chỉ giữ object
            Aggregation.unwind("userInfo"),

            // 6. Thay root = userInfo (trả về User object)
            Aggregation.replaceRoot("userInfo"),

            // 7. Phân trang
            Aggregation.skip((long) pageSize * pageNum),
            Aggregation.limit(pageSize)
        );

        AggregationResults<User> results =
            mongoTemplate.aggregate(aggregation, "conversations", User.class);

        return results.getMappedResults();
    }
}
