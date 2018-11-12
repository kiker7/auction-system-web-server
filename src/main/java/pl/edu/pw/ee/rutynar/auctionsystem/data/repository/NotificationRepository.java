package pl.edu.pw.ee.rutynar.auctionsystem.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Notification;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, ObjectId> {
}
