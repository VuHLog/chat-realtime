package com.vuhlog.identity.service;

import com.nimbusds.jose.JOSEException;
import com.vuhlog.identity.dto.request.AuthenticationRequest;
import com.vuhlog.identity.dto.request.IntrospectRequest;
import com.vuhlog.identity.dto.request.LogoutRequest;
import com.vuhlog.identity.dto.request.RefreshRequest;
import com.vuhlog.identity.dto.response.AuthenticationResponse;
import com.vuhlog.identity.dto.response.IntrospectResponse;
import com.vuhlog.identity.entity.Users;

import java.text.ParseException;
import java.util.Map;

public interface AuthenticationService {

    //verify token
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    //check username, password -> generate token
    AuthenticationResponse authenticate(AuthenticationRequest request);

    public String generateToken(Users user);

    public void logout(LogoutRequest request) throws JOSEException, ParseException;

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
