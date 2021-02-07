package org.tenpo.test.mstenpotest.security.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("User to find {}", username);
        Optional<User> userByEmail = userRepository.findUserByEmail(username);
        return userByEmail.orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public void save(UserRequest userRequest) {

        Optional<User> username = userRepository.findUserByEmail(userRequest.getUserName());
        log.debug("User by Email found {}", username);
        username.ifPresent(user -> {
            throw new InvalidInputException("Username is already registered");
        });
        userRepository.save(userMapper.toUser(userRequest));

    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void logout(String username) {
        log.info("User to logged out {}", username);
        setStatusLog(username, false);
    }

    public void setLoggedIn(String username) {
        log.info("User to logged in {}", username);
        setStatusLog(username, true);
    }

    private void setStatusLog(String username, boolean logged) {
        User user = loadUserByUsername(username);
        user.setLogged(logged);
        save(user);
    }
}
