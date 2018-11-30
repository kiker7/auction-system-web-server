package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Notification;
import pl.edu.pw.ee.rutynar.auctionsystem.services.NotificationService;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Component
public class NotificationHandler {

    @Autowired
    NotificationService notificationService;

    /**
     * Return SSE with user notifications
     */
    public Mono<ServerResponse> getUserNotifications(ServerRequest request){

        ObjectId id = new ObjectId(request.pathVariable("id"));

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(notificationService.getUserPublishedNotifications(id), Notification.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
