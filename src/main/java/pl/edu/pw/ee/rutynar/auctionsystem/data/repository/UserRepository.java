package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {

    Mono<User> findByLogin(String login);
}
