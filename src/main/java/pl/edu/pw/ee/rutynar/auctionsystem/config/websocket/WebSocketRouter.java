package pl.edu.pw.ee.rutynar.auctionsystem.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import pl.edu.pw.ee.rutynar.auctionsystem.events.BidPostedEventPublisher;
import pl.edu.pw.ee.rutynar.auctionsystem.websocket.DefaultWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class WebSocketRouter {

    @Bean
    public HandlerMapping handlerMapping(BidPostedEventPublisher eventPublisher){
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/api/ws/notifications", new DefaultWebSocketHandler(eventPublisher));

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.initApplicationContext();
        mapping.setOrder(-1);
        mapping.setUrlMap(map);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter(){
        return new WebSocketHandlerAdapter(webSocketService());
    }

    @Bean
    public WebSocketService webSocketService() {
        return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
    }

    @Bean
    Executor executor(){
        return Executors.newFixedThreadPool(100);
    }
}
