package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.connection.Server;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Library;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class UserHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        Flux<User> users = userRepository.findAll();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(users, User.class);
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        ObjectId objectId = new ObjectId(request.pathVariable("id"));

        Mono<User> existingUserMono = userRepository.findById(objectId);
        Mono<User> userMono = request.bodyToMono(User.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return userMono.zipWith(existingUserMono,
                (user, existingUser) -> {
                    try {
                        return objectMapper.readValue(objectMapper.writeValueAsString(user), User.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return user;
                })
                .flatMap(user ->
                        ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .body(userRepository.save(user), User.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getUserLibrary(ServerRequest request) {
        ObjectId id = new ObjectId(request.pathVariable("id"));

        return userRepository.findById(id)
                .flux()
                .flatMap(user -> userService.getUserGamesFromLibrary(user))
                .flatMap(games -> ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .body(fromObject(games)))
                .next()
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request){
        ObjectId objectId = new ObjectId(request.pathVariable("id"));

        Mono<User> userMono = userRepository.findById(objectId);

        return userMono
                .flatMap(user -> ServerResponse
                                    .ok()
                                    .build(userRepository.delete(user)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
