package com.domanski.mechanic.domain.loginandregister;

import com.domanski.mechanic.domain.loginandregister.dto.RegisterRequest;
import com.domanski.mechanic.domain.loginandregister.dto.RegisterUserResponse;
import com.domanski.mechanic.domain.loginandregister.dto.UserResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoginAndRegisterFacadeTest {
    private final UserRepository userRepository = new UserRepositoryInMemoryImpl();
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(userRepository);

    @Test
    public void should_register_user() {
        //given
        RegisterRequest userToRegister = prepareUserToRegister();
        //when
        RegisterUserResponse registerUser = loginAndRegisterFacade.registerUser(userToRegister);
        //then
        assertThat(registerUser.username()).isEqualTo("username");
        assertThat(registerUser.isRegistered()).isTrue();
    }

    @Test
    public void should_find_user_by_username() {
        //given
        String givenUserName = "username";
        loginAndRegisterFacade.registerUser(prepareUserToRegister());
        //when
        UserResponse result = loginAndRegisterFacade.findByUsername(givenUserName);
        //then
        assertThat(result.username()).isEqualTo("username");
    }


    @Test
    public void should_throw_user_not_found_exception_when_user_no_exist_in_database() {
        //given
        String givenUserName = "Username";
        // ...
        assertThrows(UserNoFoundException.class,() -> loginAndRegisterFacade.findByUsername(givenUserName), "User not found!");
    }

    private static RegisterRequest prepareUserToRegister() {
        return RegisterRequest.builder()
                .username("username")
                .password("password")
                .build();
    }

}