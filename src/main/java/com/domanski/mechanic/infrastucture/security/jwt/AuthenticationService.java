package com.domanski.mechanic.infrastucture.security.jwt;

import com.domanski.mechanic.domain.jwt.JwtService;
import com.domanski.mechanic.domain.loginandregister.LoginAndRegisterFacade;
import com.domanski.mechanic.domain.loginandregister.User;
import com.domanski.mechanic.infrastucture.loginandregister.controller.AuthenticationResponse;
import com.domanski.mechanic.domain.loginandregister.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password())
        );
        User user = loginAndRegisterFacade.findByUsername(loginRequest.username());
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
