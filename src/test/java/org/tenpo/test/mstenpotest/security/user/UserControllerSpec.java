package org.tenpo.test.mstenpotest.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.tenpo.test.mstenpotest.exceptions.InvalidInputException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerSpec {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Given a valid user request, then will save the user")
    public void successfulSignup() throws Exception {
        doNothing()
                .when(userService)
                .save(newUserRequest());
        mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUserRequest()))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrl("/login"));

    }

    @Test
    @DisplayName("Given an user request with incomplete data, then will return a 400 status error")
    public void incorrectUserDataSignup() throws Exception {
        mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newIncompleteUserRequest()))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Given a existing user request, then will return and 422 unprocessable entity")
    public void alreadyExistUserSignup() throws Exception {
        doThrow(InvalidInputException.class)
                .when(userService)
                .save(newUserRequest());
        mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUserRequest()))
        )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    private UserRequest newIncompleteUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("incomplete@test.com");
        userRequest.setPassword("incompletepass");
        userRequest.setRole("admin");
        return userRequest;
    }

    private UserRequest newUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("success@test.com");
        userRequest.setPassword("sucesspass");
        userRequest.setFirstName("success name");
        userRequest.setLastName("sucess last name");
        userRequest.setRole("admin");
        return userRequest;
    }

}