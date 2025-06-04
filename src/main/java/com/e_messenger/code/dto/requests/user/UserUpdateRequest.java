package com.e_messenger.code.dto.requests.user;

import com.e_messenger.code.entity.enums.Gender;
import com.e_messenger.code.utils.validation.EmailValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserUpdateRequest {
// thông tin cá nhân
    Instant dob;
    String displayName;
    @EmailValidation
    String email;

// thông tin hồ sơ
    Gender gender;
    String bio;
}
