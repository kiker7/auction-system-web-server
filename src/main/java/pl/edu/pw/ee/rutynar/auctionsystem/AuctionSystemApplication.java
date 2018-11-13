package pl.edu.pw.ee.rutynar.auctionsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class AuctionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionSystemApplication.class, args);
    }
}
