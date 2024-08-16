package com.vuhlog.identity.service;


import com.vuhlog.identity.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    public List<RoleResponse> getRoles();

    public RoleResponse getById(String id);
}
