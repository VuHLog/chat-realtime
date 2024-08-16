package com.vuhlog.identity.mapper;

import com.vuhlog.identity.dto.request.UserCreationRequest;
import com.vuhlog.identity.dto.request.UserUpdateRequest;
import com.vuhlog.identity.dto.response.UserResponse;
import com.vuhlog.identity.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "user_roles",ignore = true)
    Users toUser(UserCreationRequest request);

    UserResponse toUserResponse(Users user);

    @Mapping(target = "user_roles", ignore = true)
    void updateUser(@MappingTarget Users user, UserUpdateRequest request);
}
