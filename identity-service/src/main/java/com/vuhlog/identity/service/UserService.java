package com.vuhlog.identity.service;

import com.vuhlog.identity.dto.request.UserCreationRequest;
import com.vuhlog.identity.dto.request.UserUpdateRequest;
import com.vuhlog.identity.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    public Page<UserResponse> getUsers(Pageable pageable);

    public Page<UserResponse> getUsersContains(String s,Pageable pageable);
    public UserResponse getById(String id);

    public UserResponse getByUsername(String username);

    public Page<UserResponse> getByUsernameContaining(String username,Pageable pageable);

    public UserResponse addUser(UserCreationRequest request);

    public UserResponse updateUser(String userId, UserUpdateRequest request);

    public void deleteUser(String userId);

    public UserResponse getMyInfo();
}
