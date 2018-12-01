package pl.edu.pw.ee.rutynar.auctionsystem.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.handlers.NotificationHandler;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class NotificationRouter {

    @Bean
    public RouterFunction<ServerResponse> notificationRoutes(NotificationHandler notificationHandler){

        return route(GET("/api/notifications/{id}").and(accept(TEXT_EVENT_STREAM)), notificationHandler::getUserNotifications);
    }
}
