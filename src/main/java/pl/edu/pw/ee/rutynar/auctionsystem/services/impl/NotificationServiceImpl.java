package pl.edu.pw.ee.rutynar.auctionsystem.services.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Bid;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Notification;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.NotificationType;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.NotificationRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.events.UserNotificationEvent;
import pl.edu.pw.ee.rutynar.auctionsystem.events.UserNotificationEventPublisher;
import pl.edu.pw.ee.rutynar.auctionsystem.services.NotificationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private ApplicationEventPublisher publisher;
    private Map<ObjectId, Flux<Notification>> publishersMap;

    @Autowired
    private UserNotificationEventPublisher eventPublisher;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Executor executor;

    public NotificationServiceImpl(NotificationRepository notificationRepository, ApplicationEventPublisher publisher) {
        this.notificationRepository = notificationRepository;
        this.publisher = publisher;
        this.publishersMap = new HashMap<>();
    }

    @Override
    public Flux<Notification> getUserPublishedNotifications(ObjectId userId) {

        return userRepository.findById(userId)
                .flux()
                .flatMap(user -> notificationRepository.findAllByRecipient(user))
                .concatWith(getUserNotificationPublisher(userId));
    }

    // TODO: change message
    @Override
    public void createNotification(User recipient, NotificationType type) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        String message;
        switch (type){
            case POSTED_BID:
                message = "New posted bid";
                break;
            case AUCTION_CREATE:
                message = "Auction create";
                break;
            case AUCTION_FINISH:
                message = "Auction finish";
                break;
            default:
                message = "Default notification";
        }
        notification.setMessage(message);

        notificationRepository.save(notification)
                .subscribe(n -> this.publisher.publishEvent(new UserNotificationEvent(n)));
    }

    private Flux<Notification> getUserNotificationPublisher(ObjectId id){

        if(!this.publishersMap.containsKey(id)){
            Flux<UserNotificationEvent> emitter = Flux
                    .create(fluxSink -> this.executor.execute(() -> {
                        while(true){
                            try{
                                UserNotificationEvent event = this.eventPublisher.getUserNotificationsQueueByUserId(id).take();
                                fluxSink.next(event);
                            }catch (InterruptedException e){
                                ReflectionUtils.rethrowRuntimeException(e);
                            }
                        }
                    }));
            Flux<Notification> publisher = emitter
                    .map(event -> (Notification) event.getSource())
                    .publish().autoConnect();
            this.publishersMap.put(id, publisher);
        }
        return this.publishersMap.get(id);
    }

    private void deleteUserNotificationPublisher(ObjectId id){
        // clean up
    }
}
