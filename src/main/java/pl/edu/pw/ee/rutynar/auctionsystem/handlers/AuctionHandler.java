package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AuctionHandler {

    public Mono<ServerResponse> getAuction(ServerRequest request){


        return null;
    }

    public Mono<ServerResponse> postAuction(ServerRequest request){

        return null;
    }

    public Mono<ServerResponse> updateAuction(ServerRequest request){

        return null;
    }

    public Mono<ServerResponse> deleteAuction(ServerRequest request){

        return null;
    }

    public Mono<ServerResponse> addAuctionBid(ServerRequest request){

        return null;
    }

    public Mono<ServerResponse> addAuctionFollower(ServerRequest request){

        return null;
    }

    public Mono<ServerResponse> getAuctionBids(ServerRequest request){

        return null;
    }
}
