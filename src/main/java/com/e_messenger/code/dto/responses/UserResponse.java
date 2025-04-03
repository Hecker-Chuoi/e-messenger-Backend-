package com.e_messenger.code.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    ObjectId id;
    String phoneNumber;
    String password;

// thông tin cá nhân
    LocalDate dob;
    String displayName;
    String email;

// thông tin hồ sơ
//    String avatarUrl;
//    ActiveStatus activeStatus;
    String bio;
    LocalDateTime updatedAt;
}
