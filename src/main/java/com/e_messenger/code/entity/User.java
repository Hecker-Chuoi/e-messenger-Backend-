package com.e_messenger.code.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    @Builder.Default
    ObjectId id = new ObjectId();
    @Indexed(unique = true)
    String username;
    String password;

    // thông tin cá nhân
    LocalDate dob;
    String displayName;
    String email;
    @Indexed(unique = true)
    String phoneNumber;

    // thông tin hồ sơ
//    String avatarUrl;
//    ActiveStatus activeStatus;
    String bio;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    // Account is deleted
    @Builder.Default
    Boolean isDeleted = false;
}