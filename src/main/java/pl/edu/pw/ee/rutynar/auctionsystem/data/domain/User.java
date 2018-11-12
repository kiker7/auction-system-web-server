package pl.edu.pw.ee.rutynar.auctionsystem.data.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Document
public class User {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    private ObjectId id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Indexed(unique = true)
    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;

    @DBRef
    private Library library;

    @DBRef
    private List<Auction> followedAuctions;
}
