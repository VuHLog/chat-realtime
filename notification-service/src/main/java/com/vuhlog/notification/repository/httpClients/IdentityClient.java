package com.vuhlog.notification.repository.httpClients;

import com.vuhlog.notification.config.AuthenticationRequestInterceptor;
import com.vuhlog.notification.dto.ApiResponse;
import com.vuhlog.notification.dto.Response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", configuration = {AuthenticationRequestInterceptor.class})
public interface IdentityClient {
    @GetMapping(value = "/identity/users/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> getUserByUsername(@PathVariable String username);
}
