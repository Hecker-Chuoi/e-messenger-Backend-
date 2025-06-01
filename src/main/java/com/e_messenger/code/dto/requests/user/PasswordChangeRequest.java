package com.e_messenger.code.dto.requests.user;

import com.e_messenger.code.utils.validation.PasswordValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PasswordChangeRequest {
    String oldPassword;
    @PasswordValidation
    String newPassword;
}
