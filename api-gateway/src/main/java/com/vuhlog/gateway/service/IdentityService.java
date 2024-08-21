package com.vuhlog.gateway.service;

import com.vuhlog.gateway.dto.ApiResponse;
import com.vuhlog.gateway.dto.request.IntrospectRequest;
import com.vuhlog.gateway.dto.response.IntrospectResponse;
import com.vuhlog.gateway.repository.IdentityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class IdentityService {
    @Autowired
    private IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(IntrospectRequest.builder()
                        .token(token)
                .build());
    }
}
