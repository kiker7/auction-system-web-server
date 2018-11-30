package pl.edu.pw.ee.rutynar.auctionsystem.events;

import org.springframework.context.ApplicationEvent;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Notification;

public class UserNotificationEvent extends ApplicationEvent {

    public UserNotificationEvent(Notification source) {
        super(source);
    }
}
