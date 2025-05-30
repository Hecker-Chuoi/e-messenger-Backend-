package com.e_messenger.code.dto.requests;

import com.e_messenger.code.utils.jackson.deserializer.LocalDateDeserializer;
import com.e_messenger.code.utils.validation.EmailValidation;
import com.e_messenger.code.utils.validation.PasswordValidation;
import com.e_messenger.code.utils.validation.PhoneValidation;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
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
    @PhoneValidation
    String phoneNumber;
    @PasswordValidation
    String password;

    // thông tin cá nhân
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate dob;
    String displayName;
    @EmailValidation
    String email;

    // thông tin hồ sơ
//    String avatarUrl;
//    ActiveStatus activeStatus;
    String bio;
}
