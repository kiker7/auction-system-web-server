package pl.edu.pw.ee.rutynar.auctionsystem.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.JwtTokenUtil;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.*;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.BidRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;

import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class AuctionRouterTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;

    private Game auctionGame;

    private Auction expectedAuction;

    private List<Bid> expectedBids;

    private List<User> userList;

    @BeforeEach
    void beforeEach() {
        userList = userRepository.findAll().collectList().block();
        String token = tokenUtil.generateToken(userList.size() > 0 ? userList.get(0) : null);
        auctionGame = userList.get(0).getLibrary().getGames().get(0);
        expectedAuction = auctionRepository.findAuctionByOwner(userList.get(0)).block();
        expectedBids = bidRepository.findAllByUser(userList.get(0)).collectList().block();

        this.client = this.client.mutate()
                .baseUrl("/api/auction")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Test
    void testPostAuction() {
        Auction auction = new Auction();
        // Handler sets owner
        auction.setClosingTime(new Date());
        auction.setFinished(false);
        auction.setGame(auctionGame);

        client
                .post()
                .uri("/")
                .contentType(APPLICATION_JSON)
                .body(fromObject(auction))
                .exchange()
                .expectBody()
                .jsonPath("$.game.name")
                .isEqualTo(auctionGame.getName());
    }

    @Test
    void testGetAuction() {
        client
                .get()
                .uri("/{id}", expectedAuction.getId())
                .exchange()
                .expectBody(Auction.class)
                .isEqualTo(expectedAuction);
    }

    @Test
    void testAuctionNotFound() {
        client
                .get()
                .uri("/{id}", "13dsfdsf32")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testUpdateAuction() {
        // Update existing auction
        expectedAuction.setFinished(true);

        client
                .put()
                .uri("/{id}", expectedAuction.getId())
                .body(fromObject(expectedAuction))
                .exchange()
                .expectBody()
                .jsonPath("$.finished")
                .isEqualTo(true);
    }

    @Test
    void testDeleteAuction() {
        client
                .delete()
                .uri("/{id}", expectedAuction.getId())
                .exchange()
                .expectBody()
                .isEmpty();
    }

    @Test
    void testSetAuctionBid() {
        Bid newBid = new Bid();
        newBid.setOffer(300);

        client
                .post()
                .uri("/{id}/set-bid", expectedAuction.getId())
                .body(fromObject(newBid))
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testSetInvalidValueAuctionBid() {
        Bid newBid = new Bid();
        newBid.setOffer(300);

        client
                .post()
                .uri("/{id}/set-bid", expectedAuction.getId())
                .body(fromObject(newBid))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testAddFollowerToAuction() {
        User newFollower = userList.get(1);

        client
                .post()
                .uri("/{id}/add-follower/{userId}", expectedAuction.getId(), newFollower.getId())
                .exchange()
                .expectBodyList(User.class)
                .isEqualTo(expectedAuction.getFollowers());
    }

    @Test
    void testGetAllBidsFromAuction() {

        client
                .get()
                .uri("/{id}/bids")
                .exchange()
                .expectBodyList(Bid.class)
                .isEqualTo(expectedBids);
    }
}
