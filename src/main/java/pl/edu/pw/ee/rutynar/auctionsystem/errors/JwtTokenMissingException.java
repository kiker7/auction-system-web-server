package pl.edu.pw.ee.rutynar.auctionsystem.errors;

import org.springframework.security.core.AuthenticationException;

/**
 * Missing token in request header
 */

public class JwtTokenMissingException extends AuthenticationException {

    public JwtTokenMissingException(String msg) {
        super(msg);
    }
}
