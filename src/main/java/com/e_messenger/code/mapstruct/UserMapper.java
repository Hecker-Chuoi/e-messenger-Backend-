package com.e_messenger.code.mapstruct;

import com.e_messenger.code.dto.requests.user.UserCreationRequest;
import com.e_messenger.code.dto.requests.user.UserUpdateRequest;
import com.e_messenger.code.dto.responses.UserResponse;
import com.e_messenger.code.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreationRequest request);
    UserResponse toResponse(User user);
    List<UserResponse> toResponses(List<User> users);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
