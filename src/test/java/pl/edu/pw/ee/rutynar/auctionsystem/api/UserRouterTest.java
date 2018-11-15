package pl.edu.pw.ee.rutynar.auctionsystem.api;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.JwtTokenUtil;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserRouterTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;

    private String token;

    private List<User> expectedList = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        this.expectedList = userRepository.findAll().collectList().block();
        this.token = tokenUtil.generateToken(expectedList.size() > 0 ? expectedList.get(0) : null);

        this.client = this.client.mutate()
                .baseUrl("/api/user")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Test
    void testAuthorizedAccess(){
        client
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testInvalidToken(){
        client
                .get()
                .uri("/")
                .header("Authorization", "empty..")
                .exchange()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid token...");
    }

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
    void testUserInvalidId() {
        client
                .get()
                .uri("/4a4a4a4a")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testUserNotFound(){
        client
                .get()
                .uri("5bec9d1a582eca0711956818")
                .exchange()
                .expectStatus().isNotFound();
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

    @Test
    void testUserUpdate(){
        User user = expectedList.get(0);
        // Change user data
        user.setEmail("newEmail@tst.com");

        client
                .put()
                .uri("/{id}", user.getId())
                .body(fromObject(user))
                .exchange()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    void testUserDelete(){
        User user = expectedList.get(0);

        client
                .delete()
                .uri("/{id}", user.getId())
                .exchange()
                .expectBody()
                .isEmpty();
    }

}
