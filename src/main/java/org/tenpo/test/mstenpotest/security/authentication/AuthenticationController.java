package org.tenpo.test.mstenpotest.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tenpo.test.mstenpotest.security.user.User;
import org.tenpo.test.mstenpotest.security.util.JwtTokenUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController extends AuthenticationBase {

    private final JwtTokenUtil jwtUtil;

    @Autowired
    public AuthenticationController(JwtTokenUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> getToken(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = this.authenticate(loginRequest.getUserName(), loginRequest.getPassword());

        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(user);

        return ResponseEntity
                .ok(new JwtResponse("Bearer " + jwtToken));

    }
}
