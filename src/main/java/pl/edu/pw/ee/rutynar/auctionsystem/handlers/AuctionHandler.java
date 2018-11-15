package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class AuctionHandler {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserService userService;

    public Mono<ServerResponse> getAuction(ServerRequest request){
        ObjectId id = new ObjectId(request.pathVariable("id"));

        return auctionRepository.findById(id)
                .flatMap(auction ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(auction)))
                .switchIfEmpty(ServerResponse.notFound().build());
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
