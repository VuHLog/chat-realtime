package com.vuhlog.identity.mapper;

import com.vuhlog.identity.dto.response.RoleResponse;
import com.vuhlog.identity.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);
}
