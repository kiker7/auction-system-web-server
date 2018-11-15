package pl.edu.pw.ee.rutynar.auctionsystem.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.handlers.GameHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class GameRouter {

    @Bean
    public RouterFunction<ServerResponse> gameRoutes(GameHandler gameHandler){

        return route(POST("/api/game").and(contentType(APPLICATION_JSON)), gameHandler::postNewGame)
                .andRoute(GET("/api/game/{id}").and(accept(APPLICATION_JSON)), gameHandler::getGame)
                .andRoute(PUT("/api/game/{id}").and(contentType(APPLICATION_JSON)), gameHandler::updateGame)
                .andRoute(DELETE("/api/game/{id}").and(accept(APPLICATION_JSON)), gameHandler::deleteGame);
    }
}
