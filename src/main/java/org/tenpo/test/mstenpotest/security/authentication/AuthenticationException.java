package org.tenpo.test.mstenpotest.security.authentication;

public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 6668581872821514112L;

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
