package org.tenpo.test.mstenpotest.security.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class UserMapper {


    @Getter(AccessLevel.PRIVATE)
    private final PasswordEncoder passwordEncoder;

    public User toUser(UserRequest userRequest) {
        User user =  new User();
        user.setEmail(userRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        Role role = new Role();
        role.setName(userRequest.getRole());
        user.setRoles(Set.of(role));
        user.setLogged(false);
        return user;
    }
}
