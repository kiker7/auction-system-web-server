package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Library;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.GameRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.LibraryRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Slf4j
@Component
public class GameHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private GameRepository gameRepository;

    public Mono<ServerResponse> postNewGame(ServerRequest request) {
        Mono<User> userMono = userService.getCurrentUser();
        Mono<Game> gameMono = request.bodyToMono(Game.class).flatMap(game -> gameRepository.save(game));

        return userMono
                .map(User::getLibrary)
                .flatMap(library -> library.getGames() == null ? Mono.just(new ArrayList<Game>()) : Mono.just(library.getGames()))
                .zipWith(gameMono, (games, game) -> {
                    games.add(game);
                    return games;
                }).zipWith(userMono, (games, user) -> {
                    user.getLibrary().setGames(games);
                    return libraryRepository.save(user.getLibrary());
                }).flatMap(lib ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(APPLICATION_JSON)
                                .body(lib, Library.class)
                );
    }

    public Mono<ServerResponse> getGame(ServerRequest request){
        ObjectId gameId = new ObjectId(request.pathVariable("id"));

        return gameRepository.findById(gameId)
                .flatMap(game ->
                        ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(game)));
    }
}
