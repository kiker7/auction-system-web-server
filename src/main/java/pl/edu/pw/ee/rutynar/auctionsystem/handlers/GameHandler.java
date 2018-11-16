package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Library;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.GameRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.LibraryRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class GameHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AuctionRepository auctionRepository;

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

    public Mono<ServerResponse> getGame(ServerRequest request) {
        ObjectId gameId = new ObjectId(request.pathVariable("id"));

        return gameRepository.findById(gameId)
                .flatMap(game ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(game)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateGame(ServerRequest request) {
        ObjectId id = new ObjectId(request.pathVariable("id"));

        Mono<Game> existingGameMono = gameRepository.findById(id);
        Mono<Game> updatedGameMono = request.bodyToMono(Game.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return updatedGameMono.zipWith(existingGameMono,
                (updatedGame, existingGame) -> {
                    try {
                        return objectMapper.readValue(objectMapper.writeValueAsString(updatedGame), Game.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return updatedGame;
                })
                .flatMap(game ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(gameRepository.save(game), Game.class)
                ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteGame(ServerRequest request) {
        ObjectId id = new ObjectId(request.pathVariable("id"));
        Mono<Game> gameMono = gameRepository.findById(id);

        return gameMono
                .flatMap(game ->
                        ServerResponse.ok()
                                .build(gameRepository.delete(game)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> postGameAuction(ServerRequest request) {

        ObjectId gameId = new ObjectId(request.pathVariable("id"));
        Mono<Auction> auctionMono = request.bodyToMono(Auction.class);
        Mono<Game> gameMono = gameRepository.findById(gameId);
        Mono<User> userMono = userService.getCurrentUser();

        return auctionMono
                .zipWith(userMono, (auction, user) -> {
                    auction.setOwner(user);
                    if (auction.getFollowers() == null) {
                        auction.setFollowers(new ArrayList<>());
                    }
                    auction.getFollowers().add(user);
                    return auction;
                })
                .zipWith(gameMono, (auction, game) -> auctionRepository.findAuctionByGame(game)
                        .hasElement()
                        .flatMap(val -> {
                            if (val) {
                                return Mono.just(auction);
                            } else {
                                auction.setGame(game);
                                auction.setClosingTime(new Date());
                                auction.setFinished(false);
                                return auctionRepository.save(auction);
                            }
                        }))
                .flatMap(auctionM -> auctionM.map(auction -> auction))
                .filter(auction -> auction.getId() != null)
                .flatMap(auction -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(auction))
                ).switchIfEmpty(ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(TEXT_PLAIN)
                        .body(fromObject("Auction already exists")));
    }
}
