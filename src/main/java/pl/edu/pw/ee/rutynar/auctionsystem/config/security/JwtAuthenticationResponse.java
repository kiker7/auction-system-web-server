package pl.edu.pw.ee.rutynar.auctionsystem.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse implements Serializable {

    private String token;
    private String username;

}
