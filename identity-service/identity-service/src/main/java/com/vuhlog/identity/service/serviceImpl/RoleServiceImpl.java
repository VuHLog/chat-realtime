package com.vuhlog.identity.service.serviceImpl;

import com.vuhlog.identity.dto.response.RoleResponse;
import com.vuhlog.identity.mapper.RoleMapper;
import com.vuhlog.identity.repository.RoleRepository;
import com.vuhlog.identity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public RoleResponse getById(String id) {
        return roleMapper.toRoleResponse(roleRepository.findById(id).get());
    }

    @Override
    public List<RoleResponse> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

}
