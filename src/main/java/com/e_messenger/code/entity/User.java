package com.e_messenger.code.entity;

import com.e_messenger.code.entity.enums.Gender;
import com.e_messenger.code.service.impl.ActiveStatusService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    // thông tin tài khoản
    @Id
    String id;

    @Indexed(unique = true)
    String phoneNumber;
    String password;

    // thông tin cá nhân
    Instant dob;
    Gender gender;
    String displayName;
    String email;

    // thông tin hồ sơ
    String avatarUrl;
    String bio;
    Instant updatedAt;
    ActiveStatusService.ActiveStatus activeStatus;

    // Account is deleted
    @Builder.Default
    Boolean isDeleted = false;

    // thông tin hỗ trợ FE
    String fcmToken;
}