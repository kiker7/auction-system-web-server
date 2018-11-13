package pl.edu.pw.ee.rutynar.auctionsystem.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

public class JwtPreAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final String authToken;
    @Getter
    private final String bearerRequestHeader;
    @Getter
    private final String username;

    public JwtPreAuthenticationToken(String authToken, String bearerRequestHeader, String username) {
        super(null);
        this.authToken = authToken;
        this.bearerRequestHeader = bearerRequestHeader;
        this.username = username;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
