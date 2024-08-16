package com.vuhlog.identity.dto.request;

import com.vuhlog.identity.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    private String fullName;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String avatarUrl;

    private Set<Role> roles;
}
