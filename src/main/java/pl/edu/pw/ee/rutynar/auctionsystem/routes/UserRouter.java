package pl.edu.pw.ee.rutynar.auctionsystem.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.handlers.UserHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route(POST("/user").and(contentType(APPLICATION_JSON)), userHandler::saveUser)
                .andRoute(GET("/user").and(accept(APPLICATION_JSON)), userHandler::getAllUsers)
                .andRoute(GET("/user/{id}").and(accept(APPLICATION_JSON)), userHandler::getUser);
    }
}
