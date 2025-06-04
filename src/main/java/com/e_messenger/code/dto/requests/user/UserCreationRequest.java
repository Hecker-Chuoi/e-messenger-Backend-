package com.e_messenger.code.dto.requests.user;

import com.e_messenger.code.entity.enums.Gender;
import com.e_messenger.code.utils.validation.EmailValidation;
import com.e_messenger.code.utils.validation.PasswordValidation;
import com.e_messenger.code.utils.validation.PhoneValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreationRequest {
    @PhoneValidation
    String phoneNumber;
    @PasswordValidation
    String password;

    // thông tin cá nhân
    Instant dob;
    Gender gender;
    @NotBlank
    String displayName;
    @EmailValidation
    String email;

    // thông tin hồ sơ
    String bio;
}
