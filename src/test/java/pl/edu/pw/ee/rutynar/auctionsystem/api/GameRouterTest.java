package pl.edu.pw.ee.rutynar.auctionsystem.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.JwtTokenUtil;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;

import java.util.List;
import java.util.Random;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class GameRouterTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;

    private Game expectedGame;

    @BeforeEach
    void beforeEach() {
        List<User> userList = userRepository.findAll().collectList().block();
        String token = tokenUtil.generateToken(userList.size() > 0 ? userList.get(0) : null);
        expectedGame = userList.get(0).getLibrary().getGames().get(0);

        this.client = this.client.mutate()
                .baseUrl("/api/game")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    /* Current user from security context (based on token which is calculated for first user in db) is owner of all games used in test */

    @Test
    void testPostGame(){
        Game newGame = new Game();
        String name = "Game" + new Random().nextInt(100);
        newGame.setName(name);
        newGame.setPrice(200);

        client
                .post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromObject(newGame))
                .exchange()
                .expectBody()
                .jsonPath("$.games[:1].name")
                .isEqualTo(name);
    }

    @Test
    void testGetGame(){
        client
                .get()
                .uri("/{id}", expectedGame.getId())
                .exchange()
                .expectBody()
                .jsonPath("$.name")
                .isEqualTo(expectedGame.getName());
    }

    @Test
    void testGameNotFound(){
        client
                .get()
                .uri("/{id}", "5436543543543")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testUpdateGame(){
        // Change name
        expectedGame.setName("New Updated Name");

        client
                .put()
                .uri("/{id}", expectedGame.getId())
                .body(fromObject(expectedGame))
                .exchange()
                .expectBody(Game.class)
                .isEqualTo(expectedGame);
    }

    @Test
    void testDeleteGame(){
        client
                .delete()
                .uri("/{id}",expectedGame.getId())
                .exchange()
                .expectBody()
                .isEmpty();
    }
}
