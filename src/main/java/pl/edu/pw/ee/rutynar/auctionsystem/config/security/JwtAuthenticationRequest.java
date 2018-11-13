package pl.edu.pw.ee.rutynar.auctionsystem.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class JwtAuthenticationRequest implements Serializable {

    private String username;
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }
}
