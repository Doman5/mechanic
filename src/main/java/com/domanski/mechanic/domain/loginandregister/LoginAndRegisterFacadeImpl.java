package com.domanski.mechanic.domain.loginandregister;

import com.domanski.mechanic.domain.jwt.JwtService;
import com.domanski.mechanic.domain.loginandregister.dto.RegisterRequest;
import com.domanski.mechanic.infrastucture.loginandregister.controller.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class LoginAndRegisterFacadeImpl implements LoginAndRegisterFacade {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        User userToSave = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        User registeredUser = userRepository.save(userToSave);
        String token = jwtService.generateToken(registeredUser);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
