package com.vuhlog.friend.repository.httpClients;

import com.vuhlog.friend.config.AuthenticationRequestInterceptor;
import com.vuhlog.friend.dto.ApiResponse;
import com.vuhlog.friend.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", configuration = {AuthenticationRequestInterceptor.class})
public interface IdentityClient {
    @GetMapping(value = "/identity/users/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> getUserByUsername(@PathVariable   String username);
}
