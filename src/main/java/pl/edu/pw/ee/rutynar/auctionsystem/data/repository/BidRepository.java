package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import reactor.core.publisher.Flux;

public interface BidRepository extends ReactiveMongoRepository<Bid, ObjectId> {

    Flux<Bid> findAllByUser(User user);
}
