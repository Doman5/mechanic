package com.domanski.mechanic.domain.loginandregister;

import com.domanski.mechanic.domain.loginandregister.dto.RegisterRequest;
import com.domanski.mechanic.infrastucture.loginandregister.controller.AuthenticationResponse;

public interface LoginAndRegisterFacade {

    AuthenticationResponse registerUser(RegisterRequest registerRequest);
    User findByUsername(String username);
}
