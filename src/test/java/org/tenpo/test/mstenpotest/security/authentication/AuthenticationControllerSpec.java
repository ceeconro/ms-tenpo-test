package org.tenpo.test.mstenpotest.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.tenpo.test.mstenpotest.security.user.User;
import org.tenpo.test.mstenpotest.security.user.UserService;
import org.tenpo.test.mstenpotest.security.util.JwtTokenUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerSpec {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private Claims claims;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Given a correct user login request, will be authorized")
    public void loginOk() throws Exception {
        LoginRequest successLogin = newLoginRequest();
        Mockito.doReturn(authentication)
                .when(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(successLogin.getUserName(), successLogin.getPassword())
                );
        Mockito.doNothing()
                .when(userService)
                .setLoggedIn(any());
        Mockito.doReturn(newUser()).when(authentication).getPrincipal();
        mockMvc.perform(
                post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(newLoginRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).setLoggedIn(successLogin.getUserName());
    }

    @Test
    @DisplayName("Given a wrong user and password, login will be rejected")
    public void loginFail() throws Exception {
        LoginRequest successLogin = newLoginRequest();
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(successLogin.getUserName(), successLogin.getPassword())
        )).thenThrow(BadCredentialsException.class);
        mockMvc.perform(
                post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(newLoginRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Given a logged user, will be logged out")
    @WithMockUser(roles = "admin")
    public void logoutUserSessionActive() throws Exception {
        String username = "username@test.com";
        String token = "Bearer aaaaaaaaaaaaaaaaaaaa";
        Mockito.doReturn(username)
                .when(claims)
                .getSubject();
        Mockito.doReturn(claims)
                .when(jwtTokenUtil)
                .parseToken(any());
        Mockito.doNothing()
                .when(userService)
                .logout(username);


        mockMvc.perform(
                get("/api/v1/logout")
                        .header("Authorization", token)
        )
                .andExpect(status().isOk());

        verify(userService, times(1)).logout(username);
    }

    private User newUser() {
        User testUser = new User();
        testUser.setEmail("Test");
        return testUser;
    }

    private LoginRequest newLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("test@test.com");
        loginRequest.setPassword("test");
        return loginRequest;
    }

}