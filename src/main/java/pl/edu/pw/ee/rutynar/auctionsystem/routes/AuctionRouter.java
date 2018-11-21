package pl.edu.pw.ee.rutynar.auctionsystem.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.handlers.AuctionHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuctionRouter {

    @Bean
    public RouterFunction<ServerResponse> auctionRoutes(AuctionHandler auctionHandler){

        return route(GET("/api/auction/{id}").and(accept(APPLICATION_JSON)), auctionHandler::getAuction)
                .andRoute(PUT("/api/auction/{id}").and(contentType(APPLICATION_JSON)), auctionHandler::updateAuction)
                .andRoute(DELETE("/api/auction/{id}").and(accept(APPLICATION_JSON)), auctionHandler::deleteAuction)
                .andRoute(GET("/api/auction/{id}/bids").and(accept(APPLICATION_JSON)), auctionHandler::getAuctionBids)
                .andRoute(GET("/api/auction/{id}/bid-sse").and(accept(TEXT_EVENT_STREAM)), auctionHandler::getAuctionBidsSSE)
                .andRoute(POST("/api/auction/{id}/set-bid").and(contentType(APPLICATION_JSON)), auctionHandler::addAuctionBid)
                .andRoute(POST("/api/auction/{id}/add-follower").and(contentType(APPLICATION_JSON)), auctionHandler::addAuctionFollower);
    }
}
