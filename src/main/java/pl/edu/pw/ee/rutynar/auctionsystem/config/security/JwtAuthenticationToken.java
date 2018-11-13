package pl.edu.pw.ee.rutynar.auctionsystem.config.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Getter
    private String token;


    public JwtAuthenticationToken(String token, String username, Collection<?  extends GrantedAuthority> authorities){
        super(username, null, authorities);
        this.token = token;
    }

    public JwtAuthenticationToken(String username, Collection<? extends GrantedAuthority> authorities){
        super(username, null, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
