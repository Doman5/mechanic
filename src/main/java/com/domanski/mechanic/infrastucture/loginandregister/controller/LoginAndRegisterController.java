package com.domanski.mechanic.infrastucture.loginandregister.controller;

import com.domanski.mechanic.domain.loginandregister.LoginAndRegisterFacade;
import com.domanski.mechanic.domain.loginandregister.dto.LoginRequest;
import com.domanski.mechanic.domain.loginandregister.dto.RegisterRequest;
import com.domanski.mechanic.infrastucture.security.jwt.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authenticationResponse = loginAndRegisterFacade.registerUser(registerRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

}
