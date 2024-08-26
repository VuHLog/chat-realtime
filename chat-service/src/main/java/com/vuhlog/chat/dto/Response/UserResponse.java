package com.vuhlog.chat.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;

    private String fullName;

    private String username;

    private String password;

    private String email;

    private String avatarUrl;
}
