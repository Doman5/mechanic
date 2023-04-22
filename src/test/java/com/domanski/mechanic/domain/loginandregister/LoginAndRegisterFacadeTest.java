package com.domanski.mechanic.domain.loginandregister;

import com.domanski.mechanic.domain.jwt.JwtService;
import com.domanski.mechanic.domain.loginandregister.dto.RegisterRequest;
import com.domanski.mechanic.infrastucture.loginandregister.controller.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
class LoginAndRegisterFacadeTest {
    private final UserRepository userRepository = new UserRepositoryInMemoryImpl();
    private final JwtService jwtService = new JwtService("4HnVA+eNxFgSGdoiDQDYdii93pn5LSAOG4DCf1L9mq5GdHEiYI4pij9lxxW32k91");
    private final PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(userRepository, jwtService, passwordEncoder);

    @Test
    public void should_register_user() {
        //given
        RegisterRequest userToRegister = prepareUserToRegister();
        //when
        AuthenticationResponse authenticationResponse = loginAndRegisterFacade.registerUser(userToRegister);
        //then
        assertThat(authenticationResponse.token()).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"));
    }

    @Test
    public void should_find_user_by_username() {
        //given
        String givenUserName = "username";

        loginAndRegisterFacade.registerUser(prepareUserToRegister());
        //when
        User result = loginAndRegisterFacade.findByUsername(givenUserName);
        //then
        assertThat(result.getUsername()).isEqualTo("username");
    }


    @Test
    public void should_throw_user_not_found_exception_when_user_no_exist_in_database() {
        //given
        String givenUserName = "Username";
        // ...
        assertThrows(UsernameNotFoundException.class, () -> loginAndRegisterFacade.findByUsername(givenUserName), "User not found!");
    }

    private static RegisterRequest prepareUserToRegister() {
        return RegisterRequest.builder()
                .username("username")
                .password("password")
                .build();
    }

}