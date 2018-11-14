package pl.edu.pw.ee.rutynar.auctionsystem.api;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;

import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserRouterTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository userRepository;

    private List<User> expectedList;

    @BeforeEach
    void beforeEach() {
        this.expectedList = userRepository.findAll().collectList().block();
        this.client = this.client.mutate().baseUrl("/user").build();
    }

    /* -------------------- No security ------------------ */
    @Test
    void testGetAllUsers() {
        client
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(User.class)
                .isEqualTo(expectedList);
    }

    @Test
    void testUserInvalidNotFound() {
        client
                .get()
                .uri("/invalid")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testUserIdFound() {
        User expectedUser = expectedList.get(0);
        client
                .get()
                .uri("/{id}", expectedUser.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(User.class)
                .isEqualTo(expectedUser);
    }

    /* -------------------------------------------------- */

    @Test
    void testUnauthorizedAccess(){
        client
                .get()
                .uri("/api/user")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    //TODO: post, put, delete
}
