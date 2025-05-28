package com.e_messenger.code.dto.responses;

import com.e_messenger.code.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String phoneNumber;

// thông tin cá nhân
    Instant dob;
    String displayName;
    String email;
    Gender gender;

// thông tin hồ sơ
    String avatarUrl;
//    ActiveStatus activeStatus;
    String bio;
    Instant updatedAt;

// thông tin hỗ trợ FE
    String fcmToken;

    Boolean isDeleted = false;
}
