package org.tenpo.test.mstenpotest.security.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tenpo.test.mstenpotest.exceptions.InvalidInputException;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.findUserByEmail(username);
        return userByEmail.orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public void save(UserRequest userRequest) {

        Optional<User> userByEmail = userRepository.findUserByEmail(userRequest.getUserName());
        log.debug("user by Email found {}", userByEmail);
        userByEmail.ifPresent(user -> {
            throw new InvalidInputException("Username is already registered");
        });
        userRepository.save(userMapper.toUser(userRequest));

    }
}
