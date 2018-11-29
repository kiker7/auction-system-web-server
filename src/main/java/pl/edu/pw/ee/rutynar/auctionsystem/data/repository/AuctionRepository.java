package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuctionRepository extends ReactiveMongoRepository<Auction, ObjectId> {

    Flux<Auction> findAuctionByOwner(User owner);

    Mono<Auction> findAuctionByGame(Game game);

    Mono<Auction> findAuctionByBids(Bid bid);
}
