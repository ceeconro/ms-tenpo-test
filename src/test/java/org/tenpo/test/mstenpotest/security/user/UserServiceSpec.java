package org.tenpo.test.mstenpotest.security.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.tenpo.test.mstenpotest.exceptions.InvalidInputException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserServiceSpec {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    private void setup() {
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    @DisplayName("Given a valid user request, will save successfully")
    public void successfullSaveTest() {
        UserRequest userRequest = newUserRequest();
        User user = newUser();
        when(userRepository.findUserByEmail(userRequest.getUserName()))
                .thenReturn(Optional.empty());
        when(userMapper.toUser(userRequest))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(newSavedUser());

        userService.save(userRequest);

        verify(userRepository, times(1)).findUserByEmail(userRequest.getUserName());
        verify(userMapper, times(1)).toUser(userRequest);
        verify(userRepository, times(1)).save(newUser());

    }

    @Test
    @DisplayName("Given an existing user, will throw invalid entity exception")
    public void duplicatedUserSaveTest() {
        UserRequest userRequest = newUserRequest();
        when(userRepository.findUserByEmail(userRequest.getUserName()))
                .thenReturn(Optional.of(newSavedUser()));

        Assertions.assertThrows(InvalidInputException.class, () -> userService.save(userRequest));

        verify(userRepository, times(1)).findUserByEmail(userRequest.getUserName());
        verify(userMapper, times(0)).toUser(userRequest);
        verify(userRepository, times(0)).save(newUser());

    }

    @Test
    @DisplayName("Given an existing username, then will return a valir user")
    public void getAValidUserByUsername() {
        String validUsername = "success@test.com";
        when(userRepository.findUserByEmail("success@test.com"))
                .thenReturn(Optional.of(newSavedUser()));

        UserDetails validUser = userService.loadUserByUsername(validUsername);

        assertEquals(validUsername, validUser.getUsername());
    }

    @Test
    @DisplayName("Given an invalid username, then will throws username not found exception")
    public void searchANonExistingUser() {
        String invalidUsername = "invalid@test.com";
        when(userRepository.findUserByEmail(invalidUsername))
                .thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(invalidUsername));

    }

    private User newUser() {
        User testUser = new User();
        testUser.setEmail("success@test.com");
        return testUser;
    }

    private User newSavedUser() {
        User savedUser = newUser();
        savedUser.setId(1);
        return savedUser;
    }

    private UserRequest newUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("success@test.com");
        userRequest.setPassword("sucesspass");
        userRequest.setRole("admin");
        return userRequest;
    }

}