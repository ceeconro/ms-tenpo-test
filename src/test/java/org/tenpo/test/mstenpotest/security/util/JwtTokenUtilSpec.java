package org.tenpo.test.mstenpotest.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.tenpo.test.mstenpotest.security.user.User;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilSpec {

    private static final String TEST_USERNAME = "testUser";

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;


    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "mySecretLongKeyMySecretLongKeyMySecretLongKeyMySecretLongKey");
        ReflectionTestUtils.setField(jwtTokenUtil, "issuer", "tenpo");
        ReflectionTestUtils.setField(jwtTokenUtil, "timeToLiveInSeconds", 3600L); // one hour
        jwtTokenUtil.setUpSecretKey();

    }

    @Test
    @DisplayName("Given an username, when create token and decript,  then the user will be the same")
    public void testGenerateTokenAndValidate() {
        String token1 = jwtTokenUtil.generateToken(newUser());
        String username = jwtTokenUtil.parseToken(token1).getSubject();

        assertThat(newUser().getUsername()).isEqualTo(username);
    }

    private User newUser() {
        User testUser = new User();
        testUser.setEmail(TEST_USERNAME);
        return testUser;
    }

}