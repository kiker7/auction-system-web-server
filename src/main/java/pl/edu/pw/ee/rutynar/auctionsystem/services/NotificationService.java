package pl.edu.pw.ee.rutynar.auctionsystem.services;

import org.bson.types.ObjectId;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Notification;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.NotificationType;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import reactor.core.publisher.Flux;

public interface NotificationService {

    Flux<Notification> getUserPublishedNotifications(ObjectId userId);

    void createNotification(User recipient, NotificationType type, Object payload);
}
