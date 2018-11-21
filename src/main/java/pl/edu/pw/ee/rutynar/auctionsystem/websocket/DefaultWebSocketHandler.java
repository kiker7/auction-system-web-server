package pl.edu.pw.ee.rutynar.auctionsystem.websocket;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Role;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;


public class DefaultWebSocketHandler extends AbstractWebSocketHandler {

    public DefaultWebSocketHandler() {
        this.authorizedRoles.addAll(Arrays.asList(Role.USER.toString(), Role.ADMIN.toString()));
    }

    @Override
    Mono<Void> doHandle(WebSocketSession webSocketSession) {


        return webSocketSession.send(webSocketSession.receive().doOnNext(WebSocketMessage::retain).delayElements(Duration.ofSeconds(1)));
    }
}
