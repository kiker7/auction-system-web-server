package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class UserHandler {

    @Autowired
    private UserRepository userRepository;

    public Mono<ServerResponse> getUser(ServerRequest request) {
        ObjectId id = new ObjectId(request.pathVariable("id"));

        Mono<User> userMono = userRepository.findById(id);
        return userMono
                .flatMap(user ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(user)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveUser(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);

        return userMono.flatMap(user ->
                ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .body(userRepository.save(user), User.class)
        );
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        Flux<User> users = userRepository.findAll();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(users, User.class);
    }
}
