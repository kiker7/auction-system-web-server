package pl.edu.pw.ee.rutynar.auctionsystem.websocket;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.JwtAuthenticationToken;
import pl.edu.pw.ee.rutynar.auctionsystem.events.BidPostedEventPublisher;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
abstract class AbstractWebSocketHandler implements WebSocketHandler {

    protected ArrayList<String> authorizedRoles = new ArrayList<String>();

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.getHandshakeInfo().getPrincipal().filter(this::isAuthorized).then(doHandle(webSocketSession));
    }

    private boolean isAuthorized(Principal principal) {
        JwtAuthenticationToken jwtAuthenticationToken = null;
        if(principal != null && principal instanceof JwtAuthenticationToken){
            jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        }else {
            throw new AccessDeniedException("Invalid token");
        }

        if(jwtAuthenticationToken == null || !jwtAuthenticationToken.isAuthenticated())
            throw new AccessDeniedException("Invalid token");

        boolean hasRoles = this.hasRoles(jwtAuthenticationToken.getAuthorities());

        if(!hasRoles)
            throw new AccessDeniedException("Not authorized");

        return true;
    }

    private boolean hasRoles(Collection<GrantedAuthority> grantedAuthorities) {
        if (this.authorizedRoles == null || this.authorizedRoles.isEmpty()) return true;
        if (grantedAuthorities == null || grantedAuthorities.isEmpty()) return false;

        for (String role : authorizedRoles) {
            for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                if (role.equalsIgnoreCase(grantedAuthority.getAuthority())) return true;
            }
        }
        return false;
    }

    abstract Mono<Void> doHandle(WebSocketSession webSocketSession);
}
