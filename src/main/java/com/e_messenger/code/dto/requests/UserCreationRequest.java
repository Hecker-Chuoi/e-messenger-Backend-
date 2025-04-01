package com.e_messenger.code.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    String password;

    // thông tin cá nhân
    LocalDate dob;
    String displayName;
    String email;
    String phoneNumber;

    // thông tin hồ sơ
//    String avatarUrl;
//    ActiveStatus activeStatus;
    String bio;
}
