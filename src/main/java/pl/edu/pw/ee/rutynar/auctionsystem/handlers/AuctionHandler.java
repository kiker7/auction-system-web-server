package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.connection.Server;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.BidRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class AuctionHandler {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private UserService userService;

    public Mono<ServerResponse> getAuction(ServerRequest request) {
        ObjectId id = new ObjectId(request.pathVariable("id"));

        return auctionRepository.findById(id)
                .flatMap(auction ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(auction)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateAuction(ServerRequest request) {

        ObjectId auctionId = new ObjectId(request.pathVariable("id"));
        Mono<Auction> updatedAuctionMono = request.bodyToMono(Auction.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return updatedAuctionMono.zipWith(auctionRepository.findById(auctionId),
                (updatedAuction, auction) -> {
                    try {
                        return objectMapper.readValue(objectMapper.writeValueAsString(updatedAuction), Auction.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return auction;
                }).flatMap(auction ->
                ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(auctionRepository.save(auction), Auction.class)
        ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addAuctionBid(ServerRequest request) {
        ObjectId auctionId = new ObjectId(request.pathVariable("id"));

        Mono<Bid> bidMono = request.bodyToMono(Bid.class);
        Mono<Auction> auctionMono = auctionRepository.findById(auctionId);
        Mono<User> userMono = userService.getCurrentUser();

        return bidMono
                .zipWith(userMono, (bid, user) -> {
                    bid.setUser(user);
                    bid.setRequestTime(new Date());
                    return bidRepository.save(bid);
                })
                .flatMap(bids -> bids.map(bid -> bid))
                .zipWith(auctionMono, (bid, auction) -> {
                    if (auction.getBids() == null) {
                        auction.setBids(new ArrayList<>());
                    }
                    auction.getBids().add(bid);
                    return auctionRepository.save(auction);
                })
                .flatMap(val -> ServerResponse.status(HttpStatus.CREATED)
                    .contentType(APPLICATION_JSON)
                    .body(val, Auction.class)
                ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addAuctionFollower(ServerRequest request) {
        ObjectId auctionId = new ObjectId(request.pathVariable("id"));

        Mono<Auction> auctionMono = auctionRepository.findById(auctionId);
        Mono<User> userMono = userService.getCurrentUser();

        return auctionMono
                .zipWith(userMono, (auction, user) -> {
                    if(!auction.getFollowers().contains(user)){
                        auction.getFollowers().add(user);
                    }
                    return auctionRepository.save(auction);
                })
                .flatMap(auction -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .body(auction, Auction.class)
                ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAuctionBids(ServerRequest request) {

        return null;
    }

    // For now auctions won't be deleted
    public Mono<ServerResponse> deleteAuction(ServerRequest request) {
        return null;
    }
}
