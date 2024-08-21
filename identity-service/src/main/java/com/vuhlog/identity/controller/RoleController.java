package com.vuhlog.identity.controller;

import com.vuhlog.identity.dto.ApiResponse;
import com.vuhlog.identity.dto.response.RoleResponse;
import com.vuhlog.identity.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public ApiResponse<List<RoleResponse>> getRoles(
    ){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getRoles())
                .build();
    }

    @GetMapping("/{roleId}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String roleId){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.getById(roleId))
                .build();
    }
}