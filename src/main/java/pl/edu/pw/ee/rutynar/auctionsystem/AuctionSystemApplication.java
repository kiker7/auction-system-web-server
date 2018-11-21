package pl.edu.pw.ee.rutynar.auctionsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuctionSystemApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(AuctionSystemApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
