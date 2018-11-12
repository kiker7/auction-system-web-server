package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

}
