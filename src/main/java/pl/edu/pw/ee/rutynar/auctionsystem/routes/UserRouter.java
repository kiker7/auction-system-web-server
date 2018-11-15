package pl.edu.pw.ee.rutynar.auctionsystem.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.handlers.AuthHandler;
import pl.edu.pw.ee.rutynar.auctionsystem.handlers.UserHandler;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route(GET("/api/user").and(accept(APPLICATION_JSON)), userHandler::getAllUsers)
                .andRoute(GET("/api/user/{id}").and(accept(APPLICATION_JSON)), userHandler::getUser)
                .andRoute(PUT("/api/user/{id}").and(contentType(APPLICATION_JSON)), userHandler::updateUser)
                .andRoute(GET("/api/user/{id}/library").and(accept(APPLICATION_JSON)), userHandler::getUserLibrary);
    }

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler authHandler){
        return route(POST("/auth/token").and(contentType(APPLICATION_JSON)), authHandler::token)
                .andRoute(POST("/auth/register").and(contentType(APPLICATION_JSON)), authHandler::register);
    }
}
