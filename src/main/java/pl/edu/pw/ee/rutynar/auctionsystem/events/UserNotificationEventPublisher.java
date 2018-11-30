package pl.edu.pw.ee.rutynar.auctionsystem.events;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Notification;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class UserNotificationEventPublisher {

    private Map<ObjectId, BlockingQueue<UserNotificationEvent>> userNotificationsQueueMap = new HashMap<>();

    public BlockingQueue<UserNotificationEvent> getUserNotificationsQueueByUserId(ObjectId id){
        if(!this.userNotificationsQueueMap.containsKey(id)){
            this.userNotificationsQueueMap.put(id, new LinkedBlockingQueue<>());
        }
        return this.userNotificationsQueueMap.get(id);
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void onUserNotification(UserNotificationEvent event){
        ObjectId id = ((Notification) event.getSource()).getRecipient().getId();
        if(!this.userNotificationsQueueMap.containsKey(id)){
            this.userNotificationsQueueMap.put(id, new LinkedBlockingQueue<>());
        }
        this.userNotificationsQueueMap.get(id).offer(event);
    }
}
