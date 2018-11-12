package pl.edu.pw.ee.rutynar.auctionsystem.errors;

import org.springframework.security.core.AuthenticationException;


/**
 * Can't parse token
 */

public class JwtTokenMalformedException extends AuthenticationException {

    public JwtTokenMalformedException(String msg) {
        super(msg);
    }

    public JwtTokenMalformedException(String msg, Throwable t) {
        super(msg, t);
    }
}
