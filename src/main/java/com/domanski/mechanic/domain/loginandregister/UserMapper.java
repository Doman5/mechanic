package com.domanski.mechanic.domain.loginandregister;

import com.domanski.mechanic.domain.loginandregister.dto.RegisterRequest;
import com.domanski.mechanic.domain.loginandregister.dto.UserResponse;

import java.util.List;

class UserMapper {

    public static User createNewUserFromRegisterRequest(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.username())
                .password(registerRequest.password())
                .authorities(List.of(UserRole.ROLE_CUSTOMER))
                .enabled(true)
                .build();
    }

    public static UserResponse mapFromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
