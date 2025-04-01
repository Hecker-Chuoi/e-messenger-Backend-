package com.e_messenger.code.mapstruct;

import com.e_messenger.code.dto.requests.UserCreationRequest;
import com.e_messenger.code.dto.requests.UserUpdateRequest;
import com.e_messenger.code.dto.responses.UserResponse;
import com.e_messenger.code.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreationRequest request);
    UserResponse toResponse(User user);
    List<UserResponse> toResponses(List<User> users);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
