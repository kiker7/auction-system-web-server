package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Notification;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, ObjectId> {

    Flux<Notification> findAllByRecipient(User recipient);
}
