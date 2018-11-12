package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;

public interface BidRepository extends ReactiveMongoRepository<Bid, ObjectId> {
}
