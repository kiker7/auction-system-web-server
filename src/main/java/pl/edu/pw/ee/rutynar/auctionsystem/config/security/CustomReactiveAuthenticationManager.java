package pl.edu.pw.ee.rutynar.auctionsystem.config.security;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Setter
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private ReactiveUserDetailsService userDetailsService;

    private JwtTokenUtil jwtTokenUtil;

    public CustomReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {

        Assert.notNull(userDetailsService, "userDetailsService not null");

        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        if (authentication instanceof JwtPreAuthenticationToken) {
            return Mono.just(authentication)
                    .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                    .cast(JwtPreAuthenticationToken.class)
                    .flatMap(this::authenticateToken)
                    .publishOn(Schedulers.parallel())
                    .onErrorResume(e -> raiseBadCredentials())
                    .map(u -> new JwtAuthenticationToken(u.getUsername(), u.getAuthorities()));
        }
        return null;
    }

    private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(new BadCredentialsException("Invalid Credentials"));
    }

    private Mono<UserDetails> authenticateToken(JwtPreAuthenticationToken jwtPreAuthenticationToken) {

        try {
            String authToken = jwtPreAuthenticationToken.getAuthToken();
            String username = jwtPreAuthenticationToken.getUsername();
            String bearerRequestHeader = jwtPreAuthenticationToken.getBearerRequestHeader();

//            log.info("Checking authentication for user: " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenUtil.validateToken(authToken)) {
                    log.info("Authenticated user: " + username);
                    return this.userDetailsService.findByUsername(username);
                }
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token");
        }
        return null;

    }
}
