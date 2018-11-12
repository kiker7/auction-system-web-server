package pl.edu.pw.ee.rutynar.auctionsystem.data.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {

    @Id
    private String id;

    private String login;

    private String firstName;

    private String lastName;

    private String email;
}
