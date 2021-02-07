package org.tenpo.test.mstenpotest.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.tenpo.test.mstenpotest.security.user.UserService;
import org.tenpo.test.mstenpotest.security.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthenticationBase {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtUtil;


    Authentication authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            userService.setLoggedIn(username);
            return authenticate;

        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }


    String usernameAuthenticate(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        return jwtUtil.parseToken(token.substring(7)).getSubject();
    }
}
