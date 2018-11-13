package pl.edu.pw.ee.rutynar.auctionsystem.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Component
public class JwtAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

    @Autowired
    private  ReactiveUserDetailsService userDetailsService;

    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.param}")
    private String tokenParam;

    @Value("${jwt.prefix}")
    private String bearerPrefix;

    @Override
    public Mono<Authentication> apply(ServerWebExchange exchange) throws BadCredentialsException {
        ServerHttpRequest request = exchange.getRequest();
        try {

            Authentication authentication = null;
            String authToken = null;
            String username = null;

            String bearerRequestHeader = exchange.getRequest().getHeaders().getFirst(tokenHeader);

            if (bearerRequestHeader != null && bearerRequestHeader.startsWith(bearerPrefix + " ")) {
                authToken = bearerRequestHeader.substring(7);
            }

            if (authToken == null && request.getQueryParams() != null && !request.getQueryParams().isEmpty()) {
                String authTokenParam = request.getQueryParams().getFirst(tokenParam);
                if (authTokenParam != null) authToken = authTokenParam;
            }

            if (authToken != null) {
                try {
                    username = jwtTokenUtil.getUsernameFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    log.error("an error occured during getting username from token", e);
                } catch (Exception e) {
                    log.warn("the token is expired and not valid anymore", e);
                }
            } else {
                log.warn("couldn't find bearer string, will ignore the header");
            }

            log.info("checking authentication for user " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                return Mono.just(new JwtPreAuthenticationToken(authToken, bearerRequestHeader, username));
            }

            return Mono.just(authentication);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token...");
        }
    }
}
