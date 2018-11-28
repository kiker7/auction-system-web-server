package pl.edu.pw.ee.rutynar.auctionsystem.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Role;

import pl.edu.pw.ee.rutynar.auctionsystem.events.BidPostedEvent;
import pl.edu.pw.ee.rutynar.auctionsystem.events.BidPostedEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
public class DefaultWebSocketHandler extends AbstractWebSocketHandler {

    private BidPostedEventPublisher eventPublisher;

    public DefaultWebSocketHandler(BidPostedEventPublisher eventPublisher) {
        this.authorizedRoles.addAll(Arrays.asList(Role.USER.toString(), Role.ADMIN.toString()));
        this.eventPublisher = eventPublisher;
    }

    @Override
    Mono<Void> doHandle(WebSocketSession webSocketSession) {

        ObjectMapper objectMapper = new ObjectMapper();

        Flux<BidPostedEvent> publish = Flux
                .create(this.eventPublisher)
                .share();

        Flux<WebSocketMessage> messageFlux = publish
                .map(event -> {
                    try{
                        return objectMapper.writeValueAsString(event.getSource());
                    }catch (JsonProcessingException e){
                        throw new RuntimeException(e);
                    }
                })
                .map(str -> {
                    log.info("Sending... : " + str);
                    return webSocketSession.textMessage(str);
                });

        return webSocketSession.send(messageFlux);
    }
}
