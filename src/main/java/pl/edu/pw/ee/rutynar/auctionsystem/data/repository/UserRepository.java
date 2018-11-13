package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import reactor.core.publisher.Mono;

import javax.jws.soap.SOAPBinding;

public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {

    Mono<UserDetails> findByUsername(String username);
    Mono<User> findUserByUsername(String login);
}
