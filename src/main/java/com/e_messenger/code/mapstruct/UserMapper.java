package com.e_messenger.code.mapstruct;

import com.e_messenger.code.dto.requests.UserCreationRequest;
import com.e_messenger.code.dto.requests.UserUpdateRequest;
import com.e_messenger.code.dto.responses.UserResponse;
import com.e_messenger.code.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreationRequest request);
    @Mapping(target = "id", expression = "java(user.getId().toString())")
    UserResponse toResponse(User user);
    @Mapping(target = "id", expression = "java(user.getId().toString())")
    List<UserResponse> toResponses(List<User> users);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
