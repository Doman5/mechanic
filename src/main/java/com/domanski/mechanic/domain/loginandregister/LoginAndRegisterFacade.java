package com.domanski.mechanic.domain.loginandregister;

import com.domanski.mechanic.domain.loginandregister.dto.RegisterRequest;
import com.domanski.mechanic.domain.loginandregister.dto.RegisterUserResponse;
import com.domanski.mechanic.domain.loginandregister.dto.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import static com.domanski.mechanic.domain.loginandregister.UserMapper.createNewUserFromRegisterRequest;

@Configuration
@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final UserRepository userRepository;

    public RegisterUserResponse registerUser(RegisterRequest registerRequest) {
        User userToSave = createNewUserFromRegisterRequest(registerRequest);
        User registeredUser = userRepository.save(userToSave);
        return new RegisterUserResponse(registeredUser.getId(), registeredUser.getUsername(), true);
    }

    public UserResponse findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::mapFromUser)
                .orElseThrow(() -> new UserNoFoundException("User no found"));
    }

}
