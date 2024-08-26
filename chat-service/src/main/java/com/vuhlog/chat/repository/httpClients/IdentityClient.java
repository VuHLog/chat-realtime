package com.vuhlog.chat.repository.httpClients;

import com.vuhlog.chat.config.AuthenticationRequestInterceptor;
import com.vuhlog.chat.dto.ApiResponse;
import com.vuhlog.chat.dto.Response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", url = "${app.services.identity}", configuration = {AuthenticationRequestInterceptor.class})
public interface IdentityClient {
    @GetMapping(value = "/users/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> getUserByUsername(@PathVariable   String username);
}
