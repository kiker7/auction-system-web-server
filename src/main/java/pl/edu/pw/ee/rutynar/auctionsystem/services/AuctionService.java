package pl.edu.pw.ee.rutynar.auctionsystem.services;

import org.bson.types.ObjectId;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuctionService {

    Mono<Mono<Auction>> addBidToAuction(Mono<Bid> bidMono, ObjectId auctionId);

    Flux<Bid> getAuctionPublishedBids(ObjectId auctionId);
}
