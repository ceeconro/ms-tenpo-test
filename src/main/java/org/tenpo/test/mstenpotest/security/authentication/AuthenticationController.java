package org.tenpo.test.mstenpotest.security.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tenpo.test.mstenpotest.security.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController extends AuthenticationBase {


    @PostMapping(path = "/login")
    public ResponseEntity<?> getToken(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = this.authenticate(loginRequest.getUserName(), loginRequest.getPassword());

        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(user);

        return ResponseEntity
                .ok(new JwtResponse("Bearer " + jwtToken));

    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {

        this.userService.logout(usernameAuthenticate(request));
    }

}
